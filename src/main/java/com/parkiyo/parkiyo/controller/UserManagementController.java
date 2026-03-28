package com.parkiyo.controller;

import com.parkiyo.dto.CreateUserRequest;
import com.parkiyo.dto.EditUserRequest;
import com.parkiyo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class UserManagementController {

    private final UserService userService;

    // GET /admin/users
    @GetMapping
    public String userList(@RequestParam(required = false) String search,
                           @RequestParam(required = false) String role,
                           @RequestParam(required = false) String status,
                           Model model) {
        model.addAttribute("users", userService.getAllUsers(search, role, status));
        model.addAttribute("totalUsers", userService.getTotalUserCount());
        model.addAttribute("activeUsers", userService.getActiveUserCount());
        return "usermanagement";
    }

    // GET /admin/users/create
    @GetMapping("/create")
    public String createUserPage(Model model) {
        model.addAttribute("createUserRequest", new CreateUserRequest());
        return "createuser";
    }

    // POST /admin/users/create
    @PostMapping("/create")
    public String createUser(@Valid @ModelAttribute CreateUserRequest request,
                             RedirectAttributes redirectAttributes) {
        try {
            userService.createUser(request);
            redirectAttributes.addFlashAttribute("success",
                    "User " + request.getEmail() + " created.");
            return "redirect:/admin/users";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/users/create";
        }
    }

    // GET /admin/users/{id}/edit
    @GetMapping("/{id}/edit")
    public String editUserPage(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "edituser";
    }

    // POST /admin/users/{id}/update
    @PostMapping("/{id}/update")
    public String updateUser(@PathVariable Long id,
                             @Valid @ModelAttribute EditUserRequest request,
                             RedirectAttributes redirectAttributes) {
        try {
            userService.updateUser(id, request);
            redirectAttributes.addFlashAttribute("success", "User updated.");
            return "redirect:/admin/users";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/users/" + id + "/edit";
        }
    }

    // POST /admin/users/{id}/toggle-status
    @PostMapping("/{id}/toggle-status")
    public String toggleUserStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.toggleUserStatus(id);
            redirectAttributes.addFlashAttribute("success", "User status updated.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/users";
    }

    // POST /admin/users/{id}/delete
    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", "User deleted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/users";
    }
}
