package com.parkiyo.parkiyo.service;

<<<<<<< HEAD
import com.parkiyo.parkiyo.dto.CreateUserRequest;
import com.parkiyo.parkiyo.dto.EditUserRequest;
import com.parkiyo.parkiyo.dto.NotificationPreferenceRequest;
import com.parkiyo.parkiyo.dto.PasswordChangeRequest;
import com.parkiyo.parkiyo.dto.ProfileUpdateRequest;
import com.parkiyo.parkiyo.enums.Role;
import com.parkiyo.parkiyo.enums.UserStatus;
=======
import com.parkiyo.parkiyo.dto.*;
import com.parkiyo.parkiyo.exception.*;
>>>>>>> caaacb7f14a2e6bb2fb5db987f4456e480ceca7b
import com.parkiyo.parkiyo.model.User;
import com.parkiyo.parkiyo.enums.UserRole;
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
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetDeliveryService passwordResetDeliveryService;

<<<<<<< HEAD
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/profiles/";
    private static final int RESET_TOKEN_EXPIRY_MINUTES = 30;

    @Value("${parkiyo.app-base-url:http://localhost:8080}")
    private String appBaseUrl;
=======
    @Value("${app.upload.dir:./uploads/profiles}")
    private String uploadDir;

    // ================== PROFILE METHODS (User Side) ==================
>>>>>>> caaacb7f14a2e6bb2fb5db987f4456e480ceca7b

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

<<<<<<< HEAD
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
=======
    public User updateProfile(String email, ProfileUpdateRequest request) throws IOException {
>>>>>>> caaacb7f14a2e6bb2fb5db987f4456e480ceca7b
        User user = getUserByEmail(email);

        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getPhone() != null) user.setPhone(request.getPhone());

        return userRepository.save(user);
    }

    public User updateProfilePicture(String email, MultipartFile file) throws IOException {
        User user = getUserByEmail(email);

        if (file != null && !file.isEmpty()) {
            validateImageFile(file);
            String filename = saveProfilePicture(file);

            // Delete old picture if exists
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
            throw new InvalidFileException("Only image files are allowed (JPEG, PNG, etc.)");
        }
    }

    private void deleteOldProfilePicture(String oldPath) {
        if (oldPath != null && !oldPath.isBlank()) {
            try {
                String filename = oldPath.substring(oldPath.lastIndexOf('/') + 1);
                Path oldFile = Paths.get(uploadDir, filename);
                Files.deleteIfExists(oldFile);
            } catch (Exception ignored) {
                // Log warning in production
            }
        }
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

<<<<<<< HEAD
    private String buildResetLink(String token) {
        String baseUrl = appBaseUrl.endsWith("/") ? appBaseUrl.substring(0, appBaseUrl.length() - 1) : appBaseUrl;
        return baseUrl + "/reset-password?token=" + token;
=======
    // ================== ADMIN: USER MANAGEMENT ==================

    public Page<User> getAllUsersPaginated(Pageable pageable, String search, String role, String status) {
        // You can implement custom query in repository for better performance
        return userRepository.findAll(pageable); // Enhance with Specification later if needed
    }

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
        user.setRole(UserRole.valueOf(request.getRole().toString().toUpperCase()));
        user.setStatus(UserStatus.ACTIVE);

        return userRepository.save(user);
    }

    public User updateUser(Long id, EditUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Check email uniqueness if changed
        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already in use");
        }

        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        if (request.getRole() != null) {
            user.setRole(UserRole.valueOf(request.getRole().toUpperCase()));
        }
        if (request.getStatus() != null) {
            user.setStatus(UserStatus.valueOf(request.getStatus().toUpperCase()));
        }

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Optional: soft delete instead of hard delete
        deleteOldProfilePicture(user.getProfilePicturePath());
        userRepository.delete(user);
    }

    public void toggleUserStatus(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setStatus(user.getStatus() == UserStatus.ACTIVE ? UserStatus.INACTIVE : UserStatus.ACTIVE);
        userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public void resetUserPassword(Long id, String newPassword) {
        User user = getUserById(id);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    // Optional: Get count for dashboard
    public long getTotalUsers() {
        return userRepository.count();
>>>>>>> caaacb7f14a2e6bb2fb5db987f4456e480ceca7b
    }
}