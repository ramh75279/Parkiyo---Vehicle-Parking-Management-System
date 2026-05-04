package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.dto.*;
import com.parkiyo.parkiyo.exception.*;
import com.parkiyo.parkiyo.model.Payment;
import com.parkiyo.parkiyo.model.User;
import com.parkiyo.parkiyo.enums.Role;
import com.parkiyo.parkiyo.enums.UserStatus;
import com.parkiyo.parkiyo.model.Wallet;
import java.math.BigDecimal;
import com.parkiyo.parkiyo.repository.AuditLogRepository;
import com.parkiyo.parkiyo.repository.NotificationRepository;
import com.parkiyo.parkiyo.repository.ParkingRecordRepository;
import com.parkiyo.parkiyo.repository.PaymentRepository;
import com.parkiyo.parkiyo.repository.ReceiptRepository;
import com.parkiyo.parkiyo.repository.ReservationRepository;
import com.parkiyo.parkiyo.repository.UserRepository;
import com.parkiyo.parkiyo.repository.VehicleRepository;
import com.parkiyo.parkiyo.repository.WalletRepository;
import com.parkiyo.parkiyo.repository.WalletTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

@Slf4j


@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetDeliveryService passwordResetDeliveryService;
    private final ReceiptRepository receiptRepository;
    private final PaymentRepository paymentRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final WalletRepository walletRepository;
    private final ReservationRepository reservationRepository;
    private final ParkingRecordRepository parkingRecordRepository;
    private final VehicleRepository vehicleRepository;
    private final NotificationRepository notificationRepository;
    private final AuditLogRepository auditLogRepository;

    @Value("${parkiyo.upload.dir}")
    private String uploadDir;

    @Value("${parkiyo.app-base-url:http://localhost:8080}")
    private String appBaseUrl;

    private static final int RESET_TOKEN_EXPIRY_MINUTES = 30;

    // ================== COMMON METHODS ==================

    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    // ================== USER PROFILE METHODS ==================

    @Transactional
    public User updateProfile(String email, ProfileUpdateRequest request) {
        User user = getUserByEmail(email);

        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getPhone() != null) user.setPhone(request.getPhone());

        return userRepository.save(user);
    }

    @Transactional
    public User updateProfilePicture(String email, MultipartFile file) throws IOException {
        User user = getUserByEmail(email);

        if (file != null && !file.isEmpty()) {
            validateImageFile(file);
            String filename = saveProfilePicture(file);
            deleteOldProfilePicture(user.getProfilePicturePath());
            user.setProfilePicturePath("/uploads/" + filename);
        }

        return userRepository.save(user);
    }

    private String saveProfilePicture(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Files.copy(file.getInputStream(), uploadPath.resolve(filename));
        return filename;
    }

    private void validateImageFile(MultipartFile file) {
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new InvalidFileException("File size must be less than 5MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new InvalidFileException("Only image files are allowed");
        }
    }

    private void deleteOldProfilePicture(String oldPath) {
        if (oldPath == null || oldPath.isBlank()) {
            return;
        }
        try {
            String filename = Paths.get(oldPath).getFileName().toString();
            Path oldFile = Paths.get(uploadDir, filename);
            Files.deleteIfExists(oldFile);
        } catch (Exception e) {
            log.warn("Could not delete old profile picture: {}", e.getMessage());
        }
    }

    @Transactional
    public void changePassword(String email, PasswordChangeRequest request) {
        User user = getUserByEmail(email);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new BadRequestException("New passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void updateNotificationPreferences(String email, NotificationPreferenceRequest request) {
        User user = getUserByEmail(email);
        user.setEmailNotificationsEnabled(request.isEmailNotificationsEnabled());
        user.setSmsNotificationsEnabled(request.isSmsNotificationsEnabled());
        userRepository.save(user);
    }

    // ================== ADMIN METHODS ==================

    @Transactional
    public User createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("User with email " + request.getEmail() + " already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setStatus(UserStatus.ACTIVE);
        user.setEmailVerified(true);

        if (request.getRole() != null && !request.getRole().toString().isBlank()) {
            try {
                user.setRole(Role.valueOf(request.getRole().toString().trim().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid role: " + request.getRole());
            }
        } else {
            user.setRole(Role.USER);
        }

        User savedUser = userRepository.save(user);

        // Create a wallet for every new user so wallet page never crashes
        Wallet wallet = Wallet.builder().user(savedUser).balance(BigDecimal.ZERO).build();
        walletRepository.save(wallet);

        return savedUser;
    }

    @Transactional
    public User updateUser(Long id, EditUserRequest request) {
        User user = getUserById(id);

        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already in use");
        }

        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());

        // Safe Role Setting
        if (request.getRole() != null && !request.getRole().toString().isBlank()) {
            try {
                user.setRole(Role.valueOf(request.getRole().toString().trim().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid role: " + request.getRole());
            }
        }

        // Safe Status Setting
        if (request.getStatus() != null && !request.getStatus().isBlank()) {
            try {
                user.setStatus(UserStatus.valueOf(request.getStatus().trim().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid status: " + request.getStatus());
            }
        }

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        Long userId = user.getId();

        deleteOldProfilePicture(user.getProfilePicturePath());

        List<Payment> payments = paymentRepository.findByUser_Id(userId);
        for (Payment p : payments) {
            receiptRepository.findByPaymentId(p.getId()).ifPresent(receiptRepository::delete);
        }

        walletRepository.findByUser_Id(userId).ifPresent(w ->
                walletTransactionRepository.deleteByWallet_Id(w.getId()));

        paymentRepository.deleteAll(payments);
        reservationRepository.deleteByUser_Id(userId);
        parkingRecordRepository.deleteAllLinkedToUser(userId);
        vehicleRepository.deleteAllForUser(userId);
        notificationRepository.deleteByUser_Id(userId);
        auditLogRepository.deleteByPerformedBy_Id(userId);
        walletRepository.findByUser_Id(userId).ifPresent(walletRepository::delete);
        userRepository.delete(user);
    }

    @Transactional
    public void toggleUserStatus(Long id) {
        User user = getUserById(id);
        user.setStatus(user.getStatus() == UserStatus.ACTIVE ? UserStatus.INACTIVE : UserStatus.ACTIVE);
        userRepository.save(user);
    }

    @Transactional
    public void resetUserPassword(Long id, String newPassword) {
        User user = getUserById(id);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Page<User> getAllUsersPaginated(Pageable pageable, String search, String role, String status) {
        // Parse role enum safely
        Role roleEnum = null;
        if (role != null && !role.isBlank()) {
            try {
                roleEnum = Role.valueOf(role.trim().toUpperCase());
            } catch (IllegalArgumentException ignored) {}
        }

        // Parse status enum safely
        UserStatus statusEnum = null;
        if (status != null && !status.isBlank()) {
            try {
                statusEnum = UserStatus.valueOf(status.trim().toUpperCase());
            } catch (IllegalArgumentException ignored) {}
        }

        // Normalize search — null if blank so JPQL ignores it
        String searchTerm = (search != null && !search.isBlank()) ? search.trim() : null;

        return userRepository.findWithFilters(searchTerm, roleEnum, statusEnum, pageable);
    }

    @Transactional(readOnly = true)
    public long getTotalUsers() {
        return userRepository.count();
    }

    @Transactional(readOnly = true)
    public long countAdmins() {
        return userRepository.countByRole(Role.ADMIN);
    }

    @Transactional(readOnly = true)
    public long countActiveUsers() {
        return userRepository.countByStatus(UserStatus.ACTIVE);
    }

    // ================== PASSWORD RESET ==================

    private String buildResetLink(String token) {
        String baseUrl = appBaseUrl.endsWith("/") ?
                appBaseUrl.substring(0, appBaseUrl.length() - 1) : appBaseUrl;
        return baseUrl + "/reset-password?token=" + token;
    }

    @Transactional
    public void sendPasswordResetForUser(Long id) {
        User user = getUserById(id);
        String token = UUID.randomUUID().toString().replace("-", "");

        user.setPasswordResetToken(token);
        user.setPasswordResetTokenExpiry(LocalDateTime.now().plusMinutes(RESET_TOKEN_EXPIRY_MINUTES));

        userRepository.save(user);
        passwordResetDeliveryService.sendResetLink(user.getEmail(), buildResetLink(token));
    }
}