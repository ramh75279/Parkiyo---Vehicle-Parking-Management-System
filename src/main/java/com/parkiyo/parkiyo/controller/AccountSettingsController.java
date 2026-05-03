package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.dto.ProfileUpdateRequest;
import com.parkiyo.parkiyo.dto.PasswordChangeRequest;
import com.parkiyo.parkiyo.dto.NotificationPreferenceRequest;
import com.parkiyo.parkiyo.model.User;
import com.parkiyo.parkiyo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class AccountSettingsController {

    private final UserService userService;

    // ─── ADMIN SETTINGS ───────────────────────────────────────────────────────

    @GetMapping("/account/settings")
    public String adminSettings(Authentication auth, Model model) {
        User user = userService.getUserByEmail(auth.getName());
        model.addAttribute("user", user);
        ProfileUpdateRequest profileRequest = new ProfileUpdateRequest();
        profileRequest.setFirstName(user.getFirstName());
        profileRequest.setLastName(user.getLastName());
        profileRequest.setPhone(user.getPhone());
        model.addAttribute("profileRequest", profileRequest);
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
        model.addAttribute("isAdmin", isAdmin);
        return "account/accountsetting";
    }

    @PostMapping("/account/settings/profile")
    public String updateAdminProfile(@Valid @ModelAttribute ProfileUpdateRequest request,
                                     Authentication auth,
                                     RedirectAttributes redirectAttributes) {
        try {
            userService.updateProfile(auth.getName(), request);   // Fixed
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/account/settings";
    }

    @PostMapping("/account/settings/photo")
    public String uploadPhotoOnly(@RequestParam("photo") MultipartFile photo,
                                  Authentication auth,
                                  RedirectAttributes redirectAttributes) {
        try {
            userService.updateProfilePicture(auth.getName(), photo);   // Fixed
            redirectAttributes.addFlashAttribute("success", "Profile photo updated.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to upload photo: " + e.getMessage());
        }
        return "redirect:/account/settings";
    }

    @PostMapping("/account/settings/password")
    public String changeAdminPassword(@Valid @ModelAttribute PasswordChangeRequest request,
                                      Authentication auth,
                                      RedirectAttributes redirectAttributes) {
        try {
            userService.changePassword(auth.getName(), request);
            redirectAttributes.addFlashAttribute("success", "Password changed successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/account/settings";
    }

    @PostMapping("/account/settings/notifications")
    public String updateAdminNotificationPrefs(
            @ModelAttribute NotificationPreferenceRequest request,
            Authentication auth,
            RedirectAttributes redirectAttributes) {
        try {
            userService.updateNotificationPreferences(auth.getName(), request);
            redirectAttributes.addFlashAttribute("success", "Notification preferences saved.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/account/settings";
    }

    // ─── USER SETTINGS ────────────────────────────────────────────────────────

    @GetMapping("/settings/profile")
    public String userSettings(Authentication auth, Model model) {
        model.addAttribute("user", userService.getUserByEmail(auth.getName()));
        return "account/accountsetting-user";
    }

    @PostMapping("/settings/profile")
    public String updateUserProfile(@Valid @ModelAttribute ProfileUpdateRequest request,
                                    Authentication auth,
                                    RedirectAttributes redirectAttributes) {
        try {
            userService.updateProfile(auth.getName(), request);   // Fixed
            redirectAttributes.addFlashAttribute("success", "Profile updated.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/settings/profile";
    }

    @PostMapping("/settings/photo")
    public String uploadUserPhotoOnly(@RequestParam("photo") MultipartFile photo,
                                      Authentication auth,
                                      RedirectAttributes redirectAttributes) {
        try {
            userService.updateProfilePicture(auth.getName(), photo);   // Fixed
            redirectAttributes.addFlashAttribute("success", "Profile photo updated.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to upload photo: " + e.getMessage());
        }
        return "redirect:/settings/profile";
    }

    @PostMapping("/settings/password")
    public String changeUserPassword(@Valid @ModelAttribute PasswordChangeRequest request,
                                     Authentication auth,
                                     RedirectAttributes redirectAttributes) {
        try {
            userService.changePassword(auth.getName(), request);
            redirectAttributes.addFlashAttribute("success", "Password changed successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/settings/profile";
    }

    @PostMapping("/settings/notifications")
    public String updateUserNotificationPrefs(@ModelAttribute NotificationPreferenceRequest request,
                                              Authentication auth,
                                              RedirectAttributes redirectAttributes) {
        try {
            userService.updateNotificationPreferences(auth.getName(), request);
            redirectAttributes.addFlashAttribute("success", "Notification preferences saved.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/settings/profile";
    }

    // Generic redirect
    @GetMapping("/settings")
    public String settingsRedirect(Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return isAdmin ? "redirect:/account/settings" : "redirect:/settings/profile";
    }
}