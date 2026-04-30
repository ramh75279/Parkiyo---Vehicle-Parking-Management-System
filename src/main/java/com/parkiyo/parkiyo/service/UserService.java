package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.dto.CreateUserRequest;
import com.parkiyo.parkiyo.dto.EditUserRequest;
import com.parkiyo.parkiyo.dto.NotificationPreferenceRequest;
import com.parkiyo.parkiyo.dto.PasswordChangeRequest;
import com.parkiyo.parkiyo.dto.ProfileUpdateRequest;
import com.parkiyo.parkiyo.enums.Role;
import com.parkiyo.parkiyo.enums.UserStatus;
import com.parkiyo.parkiyo.exception.BadRequestException;
import com.parkiyo.parkiyo.exception.InvalidFileException;
import com.parkiyo.parkiyo.exception.ResourceAlreadyExistsException;
import com.parkiyo.parkiyo.exception.ResourceNotFoundException;
import com.parkiyo.parkiyo.model.User;
import com.parkiyo.parkiyo.repository.AuditLogRepository;
import com.parkiyo.parkiyo.repository.NotificationRepository;
import com.parkiyo.parkiyo.repository.ParkingRecordRepository;
import com.parkiyo.parkiyo.repository.PaymentRepository;
import com.parkiyo.parkiyo.repository.ReservationRepository;
import com.parkiyo.parkiyo.repository.UserRepository;
import com.parkiyo.parkiyo.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final NotificationRepository notificationRepository;
    private final AuditLogRepository auditLogRepository;
    private final ParkingRecordRepository parkingRecordRepository;
    private final VehicleRepository vehicleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetDeliveryService passwordResetDeliveryService;

    private static final int RESET_TOKEN_EXPIRY_MINUTES = 30;

    @Value("${parkiyo.app-base-url:http://localhost:8080}")
    private String appBaseUrl;

    @Value("${app.upload.dir:src/main/resources/static/uploads/profiles}")
    private String uploadDir;

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    public List<User> getAllUsers(String search, String role, String status) {
        List<User> users = userRepository.findAll();

        if (search != null && !search.isBlank()) {
            String q = search.toLowerCase(Locale.ROOT);
            users = users.stream()
                    .filter(u -> u.getFullName().toLowerCase(Locale.ROOT).contains(q)
                            || u.getEmail().toLowerCase(Locale.ROOT).contains(q))
                    .toList();
        }

        if (role != null && !role.isBlank()) {
            users = users.stream()
                    .filter(u -> u.getRole().name().equalsIgnoreCase(role))
                    .toList();
        }

        if (status != null && !status.isBlank()) {
            users = users.stream()
                    .filter(u -> u.getStatus().name().equalsIgnoreCase(status))
                    .toList();
        }

        return users;
    }

    public long getTotalUserCount() {
        return userRepository.count();
    }

    public long getActiveUserCount() {
        return userRepository.countByStatus(UserStatus.ACTIVE);
    }

    public long getRoleCount(Role role) {
        return userRepository.countByRole(role);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }

    @Transactional
    public void createUser(CreateUserRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new BadRequestException("Email is required.");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new BadRequestException("Password is required.");
        }
        if (request.getFirstName() == null || request.getFirstName().isBlank()
                || request.getLastName() == null || request.getLastName().isBlank()) {
            throw new BadRequestException("First name and last name are required.");
        }
        if (userRepository.existsByEmail(request.getEmail().trim())) {
            throw new ResourceAlreadyExistsException("An account with this email already exists.");
        }

        User user = User.builder()
                .firstName(request.getFirstName().trim())
                .lastName(request.getLastName().trim())
                .email(request.getEmail().trim().toLowerCase(Locale.ROOT))
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(request.getRole() != null ? request.getRole() : Role.USER)
                .status(UserStatus.ACTIVE)
                .emailNotificationsEnabled(true)
                .smsNotificationsEnabled(true)
                .build();
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(Long id, EditUserRequest request) {
        User user = getUserById(id);

        if (request.getFirstName() != null && !request.getFirstName().isBlank()) {
            user.setFirstName(request.getFirstName().trim());
        }
        if (request.getLastName() != null && !request.getLastName().isBlank()) {
            user.setLastName(request.getLastName().trim());
        }
        user.setPhone(request.getPhone());
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }
        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }

        userRepository.save(user);
    }

    @Transactional
    public void toggleUserStatus(Long id, String actorEmail) {
        User user = getUserById(id);
        if (actorEmail != null && actorEmail.equalsIgnoreCase(user.getEmail())) {
            throw new BadRequestException("You cannot deactivate your own account.");
        }

        if (user.getStatus() == UserStatus.ACTIVE) {
            user.setStatus(UserStatus.INACTIVE);
        } else {
            user.setStatus(UserStatus.ACTIVE);
        }
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id, String actorEmail) {
        User user = getUserById(id);

        if (actorEmail != null && actorEmail.equalsIgnoreCase(user.getEmail())) {
            throw new BadRequestException("You cannot delete your own account.");
        }

        long paymentCount = paymentRepository.countByUserEmail(user.getEmail());
        if (paymentCount > 0) {
            throw new BadRequestException("Cannot delete user with payment history.");
        }

        parkingRecordRepository.clearUserByEmail(user.getEmail());
        vehicleRepository.clearUserByEmail(user.getEmail());
        notificationRepository.deleteByUserEmail(user.getEmail());
        reservationRepository.deleteByUserEmail(user.getEmail());
        auditLogRepository.clearPerformedBy(user.getId());

        deleteOldProfilePicture(user.getProfilePicturePath());
        userRepository.delete(user);
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

    public User updateProfilePicture(String email, MultipartFile file) throws IOException {
        User user = getUserByEmail(email);

        if (file != null && !file.isEmpty()) {
            validateImageFile(file);
            String filename = saveProfilePicture(file);
            deleteOldProfilePicture(user.getProfilePicturePath());
            user.setProfilePicturePath("/uploads/profiles/" + filename);
        }

        return userRepository.save(user);
    }

    public User updateProfileWithPhoto(String email, ProfileUpdateRequest request) throws IOException {
        User user = getUserByEmail(email);

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }

        if (request.getProfilePicture() != null && !request.getProfilePicture().isEmpty()) {
            validateImageFile(request.getProfilePicture());
            String filename = saveProfilePicture(request.getProfilePicture());
            deleteOldProfilePicture(user.getProfilePicturePath());
            user.setProfilePicturePath("/uploads/profiles/" + filename);
        }

        return userRepository.save(user);
    }

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

    public void updateNotificationPreferences(String email, NotificationPreferenceRequest request) {
        User user = getUserByEmail(email);
        user.setEmailNotificationsEnabled(request.isEmailNotificationsEnabled());
        user.setSmsNotificationsEnabled(request.isSmsNotificationsEnabled());
        userRepository.save(user);
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
            throw new InvalidFileException("Only image files are allowed (JPEG, PNG, etc.)");
        }
    }

    private void deleteOldProfilePicture(String oldPath) {
        if (oldPath == null || oldPath.isBlank()) {
            return;
        }
        try {
            String filename = oldPath.substring(oldPath.lastIndexOf('/') + 1);
            Path oldFile = Paths.get(uploadDir).resolve(filename);
            Files.deleteIfExists(oldFile);
        } catch (Exception ignored) {
            // best-effort cleanup
        }
    }

    private String buildResetLink(String token) {
        String baseUrl = appBaseUrl.endsWith("/")
                ? appBaseUrl.substring(0, appBaseUrl.length() - 1)
                : appBaseUrl;
        return baseUrl + "/reset-password?token=" + token;
    }
}
