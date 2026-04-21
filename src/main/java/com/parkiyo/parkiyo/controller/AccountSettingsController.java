package com.parkiyo.parkiyo.controller;

import com.parkiyo.dto.ProfileUpdateRequest;
import com.parkiyo.dto.PasswordChangeRequest;
import com.parkiyo.dto.NotificationPreferenceRequest;
import com.parkiyo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class AccountSettingsController {

    private final UserService userService;

    // ─── ADMIN SETTINGS ───────────────────────────────────────────────────────

    // GET /account/settings  (admin)
    @GetMapping("/account/settings")
    public String adminSettings(Authentication auth, Model model) {
        model.addAttribute("user", userService.getUserByEmail(auth.getName()));
        return "accountsetting";
    }

    // POST /account/settings/profile  (admin)
    @PostMapping("/account/settings/profile")
    public String updateAdminProfile(@Valid @ModelAttribute ProfileUpdateRequest request,
                                     Authentication auth,
                                     RedirectAttributes redirectAttributes) {
        try {
            userService.updateProfile(auth.getName(), request);
            redirectAttributes.addFlashAttribute("success", "Profile updated.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/account/settings";
    }

    // POST /account/settings/password  (admin)
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

    // POST /account/settings/notifications  (admin)
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

    // GET /settings/profile  (user)
    @GetMapping("/settings/profile")
    public String userSettings(Authentication auth, Model model) {
        model.addAttribute("user", userService.getUserByEmail(auth.getName()));
        return "accountsetting-user";
    }

    // POST /settings/profile  (user)
    @PostMapping("/settings/profile")
    public String updateUserProfile(@Valid @ModelAttribute ProfileUpdateRequest request,
                                    Authentication auth,
                                    RedirectAttributes redirectAttributes) {
        try {
            userService.updateProfile(auth.getName(), request);
            redirectAttributes.addFlashAttribute("success", "Profile updated.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/settings/profile";
    }

    // GET /settings  (user - generic redirect)
    @GetMapping("/settings")
    public String settingsRedirect(Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return isAdmin ? "redirect:/account/settings" : "redirect:/settings/profile";
    }
}
