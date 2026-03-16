package com.parkiyo.parkiyo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    // Login page
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // Register page
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    // Forgot password page
    @GetMapping("/forgotpassword")
    public String forgotPasswordPage() {
        return "forgotpassword";
    }

    // Logout page
    @GetMapping("/logout")
    public String logoutPage() {
        return "logout";
    }

    // Access denied page
    @GetMapping("/accessdenied")
    public String accessDeniedPage() {
        return "accessdenied";
    }

}