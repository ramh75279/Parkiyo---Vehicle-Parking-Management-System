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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogService auditLogService;
    private final AuthService authService;
    private final ProfileImageStorageService profileImageStorageService;

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

    public Map<String, Long> getUserStats() {
        return Map.of(
                "totalUsers", userRepository.count(),
                "activeUsers", userRepository.countByStatus(UserStatus.ACTIVE),
                "adminUsers", userRepository.countByRole(Role.ADMIN)
        );
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

        // Create wallet for every new user
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
    public void updateProfile(String email, ProfileUpdateRequest request, MultipartFile photo) {
        User user = getUserByEmail(email);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(StringUtils.hasText(request.getPhone()) ? request.getPhone().trim() : null);
        if (photo != null && !photo.isEmpty()) {
            String previousImage = user.getProfileImagePath();
            String storedFilename = profileImageStorageService.store(photo);
            user.setProfileImagePath(storedFilename);
            profileImageStorageService.deleteIfExists(previousImage);
        }
        userRepository.save(user);
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
    public void toggleUserStatus(Long id) {
        User user = getUserById(id);
        user.setStatus(user.getStatus() == UserStatus.ACTIVE ? UserStatus.INACTIVE : UserStatus.ACTIVE);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        if (user.getRole() == Role.ADMIN && userRepository.countByRole(Role.ADMIN) <= 1) {
            throw new RuntimeException("You cannot delete the last admin account.");
        }
        profileImageStorageService.deleteIfExists(user.getProfileImagePath());
        userRepository.deleteById(id);

        auditLogService.logAction(
                "USER_DELETED",
                "User",
                id,
                "User account deleted for " + user.getEmail()
        );
    }

    public long getTotalUserCount() {
        return userRepository.count();
    }

    public long getActiveUserCount() {
        return userRepository.countByStatus(UserStatus.ACTIVE);
    }

    public long getAdminUserCount() {
        return userRepository.countByRole(Role.ADMIN);
    }

    @Transactional
    public void sendPasswordResetForUser(Long id) {
        User user = getUserById(id);
        authService.sendPasswordResetLink(user.getEmail());
    }
}
