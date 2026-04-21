package com.parkiyo.parkiyo.controller;

import com.parkiyo.dto.LoginRequest;
import com.parkiyo.dto.RegisterRequest;
import com.parkiyo.dto.PasswordResetRequest;
import com.parkiyo.service.AuthService;
import com.parkiyo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    // GET /login
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        if (error != null) model.addAttribute("error", "Invalid email or password.");
        if (logout != null) model.addAttribute("success", "You have been logged out.");
        return "login";
    }

    // GET /register
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    // POST /register
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterRequest request,
                           BindingResult result,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        if (result.hasErrors()) {
            return "register";
        }
        try {
            authService.register(request);
            redirectAttributes.addFlashAttribute("success", "Account created! Please log in.");
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    // GET /forgot-password
    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    // POST /forgot-password
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email,
                                 RedirectAttributes redirectAttributes) {
        try {
            authService.sendPasswordResetLink(email);
            redirectAttributes.addFlashAttribute("success",
                    "If this email exists, a reset link has been sent.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/forgot-password";
    }

    // GET /logout  (Spring Security handles POST /logout; this renders the animated page)
    @GetMapping("/logout")
    public String logoutPage() {
        return "logout";
    }

    // GET /access-denied
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "accessdenied";
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
