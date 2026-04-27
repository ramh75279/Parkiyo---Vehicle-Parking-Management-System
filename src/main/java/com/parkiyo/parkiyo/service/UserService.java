package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.dto.CreateUserRequest;
import com.parkiyo.parkiyo.dto.EditUserRequest;
import com.parkiyo.parkiyo.dto.NotificationPreferenceRequest;
import com.parkiyo.parkiyo.dto.PasswordChangeRequest;
import com.parkiyo.parkiyo.dto.ProfileUpdateRequest;
import com.parkiyo.parkiyo.enums.Role;
import com.parkiyo.parkiyo.enums.UserStatus;
import com.parkiyo.parkiyo.model.User;
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
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetDeliveryService passwordResetDeliveryService;

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/profiles/";
    private static final int RESET_TOKEN_EXPIRY_MINUTES = 30;

    @Value("${parkiyo.app-base-url:http://localhost:8080}")
    private String appBaseUrl;

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }

    // ================== ADMIN: GET ALL USERS (with search & filter) ==================
    public List<User> getAllUsers(String search, String role, String status) {
        List<User> users = userRepository.findAll();

        if (search != null && !search.isBlank()) {
            String q = search.toLowerCase();
            users = users.stream()
                    .filter(u -> u.getFullName().toLowerCase().contains(q) ||
                            u.getEmail().toLowerCase().contains(q))
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
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
    }

    @Transactional
    public void createUser(CreateUserRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new RuntimeException("Email is required.");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new RuntimeException("Password is required.");
        }
        if (request.getFirstName() == null || request.getFirstName().isBlank()
                || request.getLastName() == null || request.getLastName().isBlank()) {
            throw new RuntimeException("First name and last name are required.");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("An account with this email already exists.");
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
            throw new RuntimeException("You cannot deactivate your own account.");
        }

        if (user.getStatus() == UserStatus.ACTIVE) {
            user.setStatus(UserStatus.INACTIVE);
        } else {
            user.setStatus(UserStatus.ACTIVE);
        }
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
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

    // ================== PROFILE PICTURE ==================
    public User updateProfilePicture(String email, MultipartFile file) throws IOException {
        User user = getUserByEmail(email);

        if (file != null && !file.isEmpty()) {
            String filename = uploadProfilePicture(file);
            user.setProfilePicturePath("/uploads/profiles/" + filename);
        }

        return userRepository.save(user);
    }

    private String uploadProfilePicture(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Files.copy(file.getInputStream(), uploadPath.resolve(filename));
        return filename;
    }

    // ================== OTHER METHODS ==================
    public User updateProfileWithPhoto(String email, ProfileUpdateRequest request) throws IOException {
        User user = getUserByEmail(email);

        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getPhone() != null) user.setPhone(request.getPhone());

        if (request.getProfilePicture() != null && !request.getProfilePicture().isEmpty()) {
            String filename = uploadProfilePicture(request.getProfilePicture());
            user.setProfilePicturePath("/uploads/profiles/" + filename);
        }

        return userRepository.save(user);
    }

    public void changePassword(String email, PasswordChangeRequest request) {
        User user = getUserByEmail(email);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new RuntimeException("New passwords do not match");
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

    private String buildResetLink(String token) {
        String baseUrl = appBaseUrl.endsWith("/") ? appBaseUrl.substring(0, appBaseUrl.length() - 1) : appBaseUrl;
        return baseUrl + "/reset-password?token=" + token;
    }
}