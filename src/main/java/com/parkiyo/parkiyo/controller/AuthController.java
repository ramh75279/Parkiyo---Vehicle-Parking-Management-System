package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.model.User;
import com.parkiyo.parkiyo.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String loginPage() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam("email") String email,
                            @RequestParam("password") String password,
                            Model model,
                            HttpSession session) {

        User user = userService.loginUser(email, password);

        if (user == null) {
            model.addAttribute("error", "Invalid email or password");
            return "auth/login";
        }

        session.setAttribute("loggedInUser", user);
        return "redirect:/dashboard";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        String result = userService.registerUser(user);

        if (!result.equals("success")) {
            model.addAttribute("error", result);
            return "auth/register";
        }

        return "redirect:/";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "auth/forgot-password";
    }

    @GetMapping("/dashboard")
    public String dashboardPage(HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/";
        }
        return "dashboard/index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}