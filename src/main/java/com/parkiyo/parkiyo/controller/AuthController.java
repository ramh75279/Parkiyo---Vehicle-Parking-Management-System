package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.dto.RegisterRequest;
import com.parkiyo.parkiyo.dto.PasswordResetRequest;
import com.parkiyo.parkiyo.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/sign-in")
    public String signInPage(@RequestParam(value = "error", required = false) String error,
                             @RequestParam(value = "logout", required = false) String logout,
                             Model model) {
        if (error != null) model.addAttribute("error", "Invalid email or password.");
        if (logout != null) model.addAttribute("success", "You have been logged out.");
        return "auth/login";
    }

    @GetMapping("/login")
    public String loginLegacyRedirect() {
        return "redirect:/sign-in";
    }

    // GET /register
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "auth/register";
    }

    // POST /register
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterRequest request,
                           BindingResult result,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        if (result.hasErrors()) {
            return "auth/register";
        }
        try {
            authService.register(request);
            redirectAttributes.addFlashAttribute("success", "Account created! Please check your email to verify your account before logging in.");
            return "redirect:/sign-in";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }
    }

    // GET /forgot-password
    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "auth/forgot-password";
    }

    // POST /forgot-password
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email,
                                 RedirectAttributes redirectAttributes) {
        authService.sendPasswordResetLink(email);
        redirectAttributes.addFlashAttribute("success",
                "If this email exists, password reset instructions have been prepared.");
        return "redirect:/forgot-password";
    }

    @GetMapping("/reset-password")
    public String resetPasswordPage(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        model.addAttribute("passwordResetRequest", new PasswordResetRequest());
        return "auth/reset-password";
    }

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam String token,
                              RedirectAttributes redirectAttributes) {
        try {
            String result = authService.verifyEmail(token);
            if ("already_verified".equals(result)) {
                redirectAttributes.addFlashAttribute("success", "Email already verified. Please log in.");
            } else {
                redirectAttributes.addFlashAttribute("success", "Email verified! You can now log in.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Invalid or expired verification link.");
        }
        return "redirect:/sign-in";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@Valid @ModelAttribute PasswordResetRequest request,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("token", request.getToken());
            return "auth/reset-password";
        }
        try {
            authService.resetPassword(request);
            redirectAttributes.addFlashAttribute("success", "Password reset successfully. Please log in.");
            return "redirect:/sign-in";
        } catch (Exception e) {
            model.addAttribute("token", request.getToken());
            model.addAttribute("error", e.getMessage());
            return "auth/reset-password";
        }
    }

    // GET /logout renders the animated page after clearing the security session.
    @GetMapping("/logout")
    public String logoutPage(HttpServletRequest request,
                             HttpServletResponse response,
                             Authentication authentication) {
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        SecurityContextHolder.clearContext();
        return "auth/logout";
    }

    // GET /access-denied
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "auth/accessdenied";
    }

    // GET /dashboard  (role-based redirect)
    @GetMapping("/dashboard")
    public String dashboard(Authentication auth) {
        if (auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/admin/dashboard";
        }
        return "redirect:/dashboard/user";
    }
}
