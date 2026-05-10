package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.enums.NotificationType;
import com.parkiyo.parkiyo.enums.WalletReceiptStatus;
import com.parkiyo.parkiyo.model.User;
import com.parkiyo.parkiyo.model.WalletReceiptSubmission;
import com.parkiyo.parkiyo.repository.UserRepository;
import com.parkiyo.parkiyo.repository.WalletReceiptSubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletReceiptSubmissionService {

    private static final String RECEIPT_SUBDIR = "wallet-receipts";

    private final WalletReceiptSubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final WalletService walletService;
    private final NotificationService notificationService;

    @Value("${parkiyo.upload.dir}")
    private String uploadDir;

    @Transactional(readOnly = true)
    public Optional<WalletReceiptSubmission> getPendingForUser(String email) {
        return submissionRepository.findFirstByUser_EmailAndStatusOrderByCreatedAtDesc(
                email, WalletReceiptStatus.PENDING);
    }

    @Transactional(readOnly = true)
    public List<WalletReceiptSubmission> findPendingForAdmin() {
        return submissionRepository.findByStatusOrderByCreatedAtAsc(WalletReceiptStatus.PENDING);
    }

    @Transactional(readOnly = true)
    public List<WalletReceiptSubmission> findRecentProcessedForAdmin() {
        return submissionRepository.findByStatusInOrderByCreatedAtDesc(
                EnumSet.of(WalletReceiptStatus.APPROVED, WalletReceiptStatus.REJECTED),
                PageRequest.of(0, 40));
    }

    @Transactional
    public void submit(String email, BigDecimal amount, String paymentMethod, MultipartFile file) throws IOException {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Please attach a payment receipt (image or PDF).");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        if (submissionRepository.existsByUser_IdAndStatus(user.getId(), WalletReceiptStatus.PENDING)) {
            throw new IllegalStateException(
                    "You already have a top-up request awaiting admin review. Please wait for approval or rejection.");
        }

        validateReceiptFile(file);
        String storedName = saveReceiptFile(file);
        String safeOriginal = sanitizeOriginalFilename(file.getOriginalFilename());

        WalletReceiptSubmission submission = WalletReceiptSubmission.builder()
                .user(user)
                .amount(amount)
                .paymentMethod(paymentMethod != null ? paymentMethod.trim() : null)
                .storedFilename(storedName)
                .originalFilename(safeOriginal)
                .status(WalletReceiptStatus.PENDING)
                .build();

        submissionRepository.save(submission);
    }

    @Transactional
    public void approve(Long submissionId, String adminEmail) {
        WalletReceiptSubmission s = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new IllegalArgumentException("Submission not found."));
        if (s.getStatus() != WalletReceiptStatus.PENDING) {
            throw new IllegalStateException("This request was already processed.");
        }

        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found."));

        String userEmail = s.getUser().getEmail();
        String desc = "Wallet top-up (receipt request #" + s.getId() + ", approved)";
        walletService.topUp(userEmail, s.getAmount(), desc);

        s.setStatus(WalletReceiptStatus.APPROVED);
        s.setReviewedBy(admin);
        s.setReviewedAt(LocalDateTime.now());
        s.setAdminNote(null);
        submissionRepository.save(s);

        notificationService.createNotification(
                s.getUser(),
                NotificationType.PAYMENT,
                "Wallet top-up approved",
                "Your payment receipt was approved. Rs. " + s.getAmount() + " has been added to your Parkiyo wallet.",
                "/wallet/overview"
        );
    }

    @Transactional
    public void reject(Long submissionId, String adminEmail, String note) {
        WalletReceiptSubmission s = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new IllegalArgumentException("Submission not found."));
        if (s.getStatus() != WalletReceiptStatus.PENDING) {
            throw new IllegalStateException("This request was already processed.");
        }

        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found."));

        s.setStatus(WalletReceiptStatus.REJECTED);
        s.setReviewedBy(admin);
        s.setReviewedAt(LocalDateTime.now());
        s.setAdminNote(note != null && !note.isBlank() ? note.trim() : null);
        submissionRepository.save(s);

        String extra = s.getAdminNote() != null ? " Reason: " + s.getAdminNote() : "";
        notificationService.createNotification(
                s.getUser(),
                NotificationType.PAYMENT,
                "Wallet top-up not approved",
                "Your wallet top-up request was rejected." + extra,
                "/wallet/overview"
        );
    }

    private void validateReceiptFile(MultipartFile file) {
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("Receipt file must be 10MB or smaller.");
        }
        String ct = file.getContentType();
        if (ct == null) {
            throw new IllegalArgumentException("Could not read file type. Use an image or PDF.");
        }
        if (ct.startsWith("image/") || "application/pdf".equalsIgnoreCase(ct)) {
            return;
        }
        throw new IllegalArgumentException("Only images (JPG, PNG, etc.) or PDF files are allowed for receipts.");
    }

    private String saveReceiptFile(MultipartFile file) throws IOException {
        Path dir = Paths.get(uploadDir, RECEIPT_SUBDIR);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        String ext = extractExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID() + (ext.isEmpty() ? "" : "." + ext);
        Path target = dir.resolve(filename);
        Files.copy(file.getInputStream(), target);
        return filename;
    }

    private static String extractExtension(String original) {
        if (original == null || original.isBlank()) {
            return "";
        }
        String name = Paths.get(original).getFileName().toString();
        int dot = name.lastIndexOf('.');
        if (dot < 0 || dot >= name.length() - 1) {
            return "";
        }
        return name.substring(dot + 1).toLowerCase();
    }

    private static String sanitizeOriginalFilename(String original) {
        if (original == null || original.isBlank()) {
            return "receipt";
        }
        String name = Paths.get(original).getFileName().toString();
        if (name.length() > 200) {
            return name.substring(0, 200);
        }
        return name;
    }
}
