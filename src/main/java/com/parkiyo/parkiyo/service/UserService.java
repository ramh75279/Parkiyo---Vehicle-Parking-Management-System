package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.dto.*;
import com.parkiyo.parkiyo.exception.*;
import com.parkiyo.parkiyo.model.User;
import com.parkiyo.parkiyo.enums.Role;
import com.parkiyo.parkiyo.enums.UserStatus;
import com.parkiyo.parkiyo.repository.UserRepository;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetDeliveryService passwordResetDeliveryService;

    @Value("${app.upload.dir:./uploads/profiles}")
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
            user.setProfilePicturePath("/uploads/profiles/" + filename);
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
            System.err.println("Could not delete old profile picture: " + e.getMessage());
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

        // Safe Role Setting
        if (request.getRole() != null && !request.getRole().toString().isBlank()) {
            try {
                user.setRole(Role.valueOf(request.getRole().toString().trim().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid role: " + request.getRole());
            }
        } else {
            user.setRole(Role.USER);
        }

        return userRepository.save(user);
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

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        deleteOldProfilePicture(user.getProfilePicturePath());
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
        // TODO: Later improve with actual filtering using custom repository query
        return userRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public long getTotalUsers() {
        return userRepository.count();
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