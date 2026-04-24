package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.dto.*;
import com.parkiyo.parkiyo.enums.Role;
import com.parkiyo.parkiyo.enums.UserStatus;
import com.parkiyo.parkiyo.model.User;
import com.parkiyo.parkiyo.model.Wallet;
import com.parkiyo.parkiyo.repository.UserRepository;
import com.parkiyo.parkiyo.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogService auditLogService;

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
    }

    public List<User> getAllUsers(String search, String role, String status) {
        List<User> users = userRepository.findAll();

        if (search != null && !search.isBlank()) {
            String q = search.toLowerCase();
            users = users.stream()
                    .filter(u -> u.getFirstName().toLowerCase().contains(q)
                            || u.getLastName().toLowerCase().contains(q)
                            || u.getEmail().toLowerCase().contains(q))
                    .toList();
        }
        if (role != null && !role.isBlank()) {
            Role r = Role.valueOf(role.toUpperCase());
            users = users.stream().filter(u -> u.getRole() == r).toList();
        }
        if (status != null && !status.isBlank()) {
            UserStatus s = UserStatus.valueOf(status.toUpperCase());
            users = users.stream().filter(u -> u.getStatus() == s).toList();
        }
        return users;
    }

    @Transactional
    public void createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use: " + request.getEmail());
        }
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(request.getRole())
                .status(UserStatus.ACTIVE)
                .emailNotificationsEnabled(true)
                .smsNotificationsEnabled(true)
                .build();
        userRepository.save(user);

        Wallet wallet = Wallet.builder().user(user).build();
        walletRepository.save(wallet);

        auditLogService.logAction(
                "USER_CREATED",
                "User",
                user.getId(),
                "User account created for " + user.getEmail()
        );
    }

    @Transactional
    public void updateUser(Long id, EditUserRequest request) {
        User user = getUserById(id);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setRole(request.getRole());
        user.setStatus(request.getStatus());
        userRepository.save(user);

        auditLogService.logAction(
                "USER_UPDATED",
                "User",
                user.getId(),
                "User account updated for " + user.getEmail()
        );
    }

    @Transactional
    public void updateProfileWithPhoto(String email, ProfileUpdateRequest request) {
        User user = getUserByEmail(email);

        if (request.getFirstName() != null && !request.getFirstName().isBlank())
            user.setFirstName(request.getFirstName());

        if (request.getLastName() != null && !request.getLastName().isBlank())
            user.setLastName(request.getLastName());

        if (request.getPhone() != null && !request.getPhone().isBlank())
            user.setPhone(request.getPhone());

        if (request.getProfilePicture() != null && !request.getProfilePicture().isEmpty()) {
            String filePath = saveProfilePicture(request.getProfilePicture(), user.getId());

            if (user.getProfilePicturePath() != null) {
                deleteOldProfilePicture(user.getProfilePicturePath());
            }

            user.setProfilePicturePath(filePath);
            System.out.println("✅ Profile picture path updated in DB: " + filePath);
        }

        userRepository.save(user);
    }

    private String saveProfilePicture(MultipartFile file, Long userId) {
        try {
            String projectRoot = System.getProperty("user.dir");
            // Most reliable path construction
            String staticDir = projectRoot + File.separator + "src" + File.separator + "main"
                    + File.separator + "resources" + File.separator + "static"
                    + File.separator + "uploads" + File.separator + "profile-pics";

            Path uploadPath = Paths.get(staticDir);
            System.out.println("📍 Attempting to save in: " + uploadPath.toAbsolutePath());

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                System.out.println("✅ Directory created successfully");
            }

            String originalName = file.getOriginalFilename();
            String extension = originalName.substring(originalName.lastIndexOf(".")).toLowerCase();
            String newFileName = userId + "-" + System.currentTimeMillis() + extension;

            Path filePath = uploadPath.resolve(newFileName);
            System.out.println("💾 Saving file to: " + filePath.toAbsolutePath());

            file.transferTo(filePath.toFile());

            System.out.println("🎉 SUCCESS! Photo saved: " + filePath.toAbsolutePath());

            return "/uploads/profile-pics/" + newFileName;

        } catch (Exception e) {
            System.err.println("❌ FAILED to save photo: " + e.getClass().getSimpleName());
            e.printStackTrace();
            throw new RuntimeException("Failed to save profile picture: " + e.getMessage());
        }
    }

    private void deleteOldProfilePicture(String oldPath) {
        if (oldPath == null || !oldPath.startsWith("/uploads")) return;
        try {
            String projectRoot = System.getProperty("user.dir");
            String fullPath = projectRoot + "/src/main/resources/static" + oldPath;
            Files.deleteIfExists(Paths.get(fullPath));
            System.out.println("🗑️ Deleted old photo");
        } catch (Exception ignored) {}
    }

    @Transactional
    public void changePassword(String email, PasswordChangeRequest request) {
        User user = getUserByEmail(email);
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect.");
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match.");
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

    @Transactional
    public void toggleUserStatus(Long id, String currentAdminEmail) {
        User user = getUserById(id);
        if (user.getEmail().equalsIgnoreCase(currentAdminEmail)) {
            throw new RuntimeException("You cannot deactivate your own account.");
        }
        user.setStatus(user.getStatus() == UserStatus.ACTIVE ? UserStatus.INACTIVE : UserStatus.ACTIVE);
        userRepository.save(user);

        auditLogService.logAction(
                "USER_STATUS_TOGGLED",
                "User",
                user.getId(),
                "User " + user.getEmail() + " status changed to " + user.getStatus().name()
        );
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        try {
            userRepository.deleteById(id);
            auditLogService.logAction(
                    "USER_DELETED",
                    "User",
                    id,
                    "User account deleted for " + user.getEmail()
            );
        } catch (Exception e) {
            throw new RuntimeException(
                    "Cannot delete this user because they have existing parking records, " +
                            "payments, or vehicles linked to their account. " +
                            "Deactivate the account instead."
            );
        }
    }

    public long getTotalUserCount() {
        return userRepository.count();
    }

    public long getActiveUserCount() {
        return userRepository.countByStatus(UserStatus.ACTIVE);
    }
}