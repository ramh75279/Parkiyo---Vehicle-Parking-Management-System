package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.dto.ProfileUpdateRequest;
import com.parkiyo.parkiyo.dto.PasswordChangeRequest;
import com.parkiyo.parkiyo.dto.NotificationPreferenceRequest;
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

@Controller
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class AccountSettingsController {

    private final UserService userService;

    // GET Admin Settings
    @GetMapping("/account/settings")
    public String adminSettings(Authentication auth, Model model) {
        model.addAttribute("user", userService.getUserByEmail(auth.getName()));
        return "account/accountsetting";
    }

    // POST Profile Update (with photo support)
    @PostMapping("/account/settings/profile")
    public String updateAdminProfile(@Valid @ModelAttribute ProfileUpdateRequest request,
                                     Authentication auth,
                                     RedirectAttributes redirectAttributes) {
        try {
            userService.updateProfileWithPhoto(auth.getName(), request);
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/account/settings";
    }

    // Photo Only Upload (used by the preview form)
    @PostMapping("/account/settings/photo")
    public String uploadPhotoOnly(@RequestParam("photo") MultipartFile photo,
                                  Authentication auth,
                                  RedirectAttributes redirectAttributes) {
        try {
            ProfileUpdateRequest request = new ProfileUpdateRequest();
            request.setProfilePicture(photo);
            userService.updateProfileWithPhoto(auth.getName(), request);
            redirectAttributes.addFlashAttribute("success", "Profile photo updated.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to upload photo: " + e.getMessage());
        }
        return "redirect:/account/settings";
    }

    // Password Change
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

    // Notifications
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

    // ─── USER SIDE (Same logic) ─────────────────────────────────────
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
            userService.updateProfileWithPhoto(auth.getName(), request);
            redirectAttributes.addFlashAttribute("success", "Profile updated.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/settings/profile";
    }
}