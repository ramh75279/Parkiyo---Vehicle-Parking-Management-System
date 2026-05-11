package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.enums.FeedbackCategory;
import com.parkiyo.parkiyo.enums.FeedbackPriority;
import com.parkiyo.parkiyo.enums.FeedbackReportStatus;
import com.parkiyo.parkiyo.enums.NotificationType;
import com.parkiyo.parkiyo.model.FeedbackReport;
import com.parkiyo.parkiyo.model.User;
import com.parkiyo.parkiyo.repository.FeedbackReportRepository;
import com.parkiyo.parkiyo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackReportService {

    private static final String FEEDBACK_SUBDIR = "feedback";

    private final FeedbackReportRepository feedbackReportRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Value("${parkiyo.upload.dir}")
    private String uploadDir;

    @Transactional(readOnly = true)
    public List<FeedbackReport> listForUser(String email) {
        return feedbackReportRepository.findByUser_EmailOrderByCreatedAtDesc(email);
    }

    @Transactional(readOnly = true)
    public FeedbackReport getForUser(Long id, String email) {
        return feedbackReportRepository.findByIdAndUser_Email(id, email)
                .orElseThrow(() -> new IllegalArgumentException("Report not found."));
    }

    @Transactional(readOnly = true)
    public FeedbackReport getForAdmin(Long id) {
        return feedbackReportRepository.findByIdWithUser(id)
                .orElseThrow(() -> new IllegalArgumentException("Report not found."));
    }

    @Transactional(readOnly = true)
    public List<FeedbackReport> listForAdmin(String statusKey, String categoryKey) {
        FeedbackReportStatus st = parseStatus(statusKey);
        FeedbackCategory cat = parseCategory(categoryKey);
        if (st == null && cat == null) {
            return feedbackReportRepository.findAllForAdminOrderByCreatedAtDesc();
        }
        return feedbackReportRepository.findFiltered(st, cat);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> adminDashboardStats() {
        Map<String, Object> m = new LinkedHashMap<>();
        long pending = feedbackReportRepository.countByStatus(FeedbackReportStatus.PENDING);
        long inReview = feedbackReportRepository.countByStatus(FeedbackReportStatus.IN_REVIEW);
        long resolved = feedbackReportRepository.countByStatus(FeedbackReportStatus.RESOLVED);
        long rejected = feedbackReportRepository.countByStatus(FeedbackReportStatus.REJECTED);
        m.put("pending", pending);
        m.put("inReview", inReview);
        m.put("resolved", resolved);
        m.put("rejected", rejected);
        long total = feedbackReportRepository.count();
        m.put("total", total);
        double rawAvg = feedbackReportRepository.averageRatingSubmitted();
        m.put("avgRating", Double.isNaN(rawAvg) ? 0.0 : Math.round(rawAvg * 10.0) / 10.0);
        long closed = resolved + rejected;
        m.put("resolutionRatePct", total == 0 ? 0 : Math.round(100.0 * closed / total));
        List<Map<String, Object>> byCat = new ArrayList<>();
        for (FeedbackCategory c : FeedbackCategory.values()) {
            long cnt = feedbackReportRepository.countByCategory(c);
            if (cnt > 0) {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("category", c);
                row.put("count", cnt);
                byCat.add(row);
            }
        }
        byCat.sort(Comparator.comparingLong(o -> -(Long) o.get("count")));
        m.put("topCategories", byCat.stream().limit(5).collect(Collectors.toList()));
        m.put("recent", feedbackReportRepository.findAllForAdminOrderByCreatedAtDesc().stream().limit(12).toList());
        return m;
    }

    @Transactional
    public FeedbackReport create(String email,
                                 String title,
                                 String description,
                                 String categoryKey,
                                 Integer rating,
                                 MultipartFile attachment) throws IOException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        FeedbackCategory category = parseCategoryRequired(categoryKey);
        validateText(title, description);
        Integer r = normalizeRating(category, rating);

        String path = null;
        if (attachment != null && !attachment.isEmpty()) {
            path = saveAttachment(attachment);
        }

        FeedbackReport report = FeedbackReport.builder()
                .user(user)
                .title(title.trim())
                .description(description.trim())
                .category(category)
                .rating(r)
                .status(FeedbackReportStatus.PENDING)
                .priority(FeedbackPriority.MEDIUM)
                .attachmentPath(path)
                .build();
        return feedbackReportRepository.save(report);
    }

    @Transactional
    public void updateByUser(Long id,
                             String email,
                             String title,
                             String description,
                             String categoryKey,
                             Integer rating,
                             MultipartFile attachment,
                             boolean removeAttachment) throws IOException {
        FeedbackReport report = getForUser(id, email);
        assertUserCanMutateContent(report);
        FeedbackCategory category = parseCategoryRequired(categoryKey);
        validateText(title, description);
        report.setTitle(title.trim());
        report.setDescription(description.trim());
        report.setCategory(category);
        report.setRating(normalizeRating(category, rating));

        if (removeAttachment && report.getAttachmentPath() != null) {
            deleteStoredFile(report.getAttachmentPath());
            report.setAttachmentPath(null);
        }
        if (attachment != null && !attachment.isEmpty()) {
            if (report.getAttachmentPath() != null) {
                deleteStoredFile(report.getAttachmentPath());
            }
            report.setAttachmentPath(saveAttachment(attachment));
        }
        feedbackReportRepository.save(report);
    }

    @Transactional
    public void deleteByUser(Long id, String email) {
        FeedbackReport report = getForUser(id, email);
        assertUserCanMutateContent(report);
        if (report.getAttachmentPath() != null) {
            deleteStoredFile(report.getAttachmentPath());
        }
        feedbackReportRepository.delete(report);
    }

    @Transactional
    public void updateByAdmin(Long id,
                              FeedbackReportStatus status,
                              FeedbackPriority priority,
                              String adminResponse) {
        FeedbackReport report = getForAdmin(id);
        String prev = report.getAdminResponse();
        if (status != null) {
            report.setStatus(status);
        }
        if (priority != null) {
            report.setPriority(priority);
        }
        String trimmed = adminResponse != null ? adminResponse.trim() : "";
        report.setAdminResponse(trimmed.isEmpty() ? null : trimmed);
        if (report.getAdminResponse() == null || report.getAdminResponse().isBlank()) {
            report.setRespondedAt(null);
        } else {
            report.setRespondedAt(LocalDateTime.now());
            boolean firstReply = prev == null || prev.isBlank();
            if (firstReply || !Objects.equals(prev, report.getAdminResponse())) {
                notificationService.createNotification(
                        report.getUser(),
                        NotificationType.SYSTEM,
                        "Feedback update",
                        "An admin replied to your report: " + report.getTitle(),
                        "/feedback"
                );
            }
        }
        feedbackReportRepository.save(report);
    }

    @Transactional
    public void deleteByAdmin(Long id) {
        FeedbackReport report = getForAdmin(id);
        if (report.getAttachmentPath() != null) {
            deleteStoredFile(report.getAttachmentPath());
        }
        feedbackReportRepository.delete(report);
    }

    public boolean userMayEditOrDelete(FeedbackReport report) {
        if (report.getStatus() == FeedbackReportStatus.RESOLVED
                || report.getStatus() == FeedbackReportStatus.REJECTED) {
            return false;
        }
        String ar = report.getAdminResponse();
        return ar == null || ar.isBlank();
    }

    private void assertUserCanMutateContent(FeedbackReport report) {
        if (!userMayEditOrDelete(report)) {
            throw new IllegalStateException("This report can no longer be edited or deleted.");
        }
    }

    private static void validateText(String title, String description) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required.");
        }
        if (title.length() > 200) {
            throw new IllegalArgumentException("Title must be 200 characters or less.");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description is required.");
        }
        if (description.length() > 8000) {
            throw new IllegalArgumentException("Description is too long.");
        }
    }

    private static Integer normalizeRating(FeedbackCategory category, Integer rating) {
        if (category != FeedbackCategory.REVIEW) {
            return null;
        }
        if (rating == null || rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Please choose a 1–5 star rating for reviews.");
        }
        return rating;
    }

    private static FeedbackCategory parseCategoryRequired(String key) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("Category is required.");
        }
        try {
            return FeedbackCategory.valueOf(key.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid category.");
        }
    }

    private static FeedbackReportStatus parseStatus(String key) {
        if (key == null || key.isBlank() || "ALL".equalsIgnoreCase(key)) {
            return null;
        }
        try {
            return FeedbackReportStatus.valueOf(key.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static FeedbackCategory parseCategory(String key) {
        if (key == null || key.isBlank() || "ALL".equalsIgnoreCase(key)) {
            return null;
        }
        try {
            return FeedbackCategory.valueOf(key.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private void validateAttachment(MultipartFile file) {
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("Attachment must be 10MB or smaller.");
        }
        String ct = file.getContentType();
        if (ct == null || !ct.startsWith("image/")) {
            throw new IllegalArgumentException("Only image attachments are allowed (JPG, PNG, WebP, etc.).");
        }
    }

    private String saveAttachment(MultipartFile file) throws IOException {
        validateAttachment(file);
        Path dir = Paths.get(uploadDir, FEEDBACK_SUBDIR);
        Files.createDirectories(dir);
        String ext = extractExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID() + (ext.isEmpty() ? "" : "." + ext);
        Path target = dir.resolve(filename);
        Files.copy(file.getInputStream(), target);
        return FEEDBACK_SUBDIR + "/" + filename;
    }

    private void deleteStoredFile(String relativePath) {
        try {
            Path p = Paths.get(uploadDir).resolve(relativePath);
            Files.deleteIfExists(p);
        } catch (IOException ignored) {
            // best-effort cleanup
        }
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
        return name.substring(dot + 1).toLowerCase(Locale.ROOT);
    }
}
