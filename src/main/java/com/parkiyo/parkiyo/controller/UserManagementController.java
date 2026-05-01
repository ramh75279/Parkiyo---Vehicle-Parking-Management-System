package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.dto.CreateUserRequest;
import com.parkiyo.parkiyo.dto.EditUserRequest;
import com.parkiyo.parkiyo.enums.Role;
import com.parkiyo.parkiyo.enums.UserStatus;
import com.parkiyo.parkiyo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class UserManagementController {

    private final UserService userService;

    // GET /admin/users - List Users (FIXED)
    @GetMapping
    public String userList(@RequestParam(required = false) String search,
                           @RequestParam(required = false) String role,
                           @RequestParam(required = false) String status,
                           Model model) {

        // Fixed: Create proper Pageable instead of passing null
        Pageable pageable = PageRequest.of(0, 50); // Page 0, 50 users per page

        var usersPage = userService.getAllUsersPaginated(pageable, search, role, status);

        model.addAttribute("users", usersPage.getContent());
        model.addAttribute("totalUsers", userService.getTotalUsers());

        // TODO: You can implement real stats later
        model.addAttribute("activeUsers", 0);
        model.addAttribute("adminUsers", 0);

        return "admin/usermanagement";
    }

    // GET /admin/users/create
    @GetMapping("/create")
    public String createUserPage(Model model) {
        model.addAttribute("createUserRequest", new CreateUserRequest());
        model.addAttribute("allRoles", Role.values());
        return "admin/createuser";
    }

    // POST /admin/users/create
    @PostMapping("/create")
    public String createUser(@Valid @ModelAttribute CreateUserRequest request,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error",
                    result.getFieldError() != null ? result.getFieldError().getDefaultMessage() : "Validation error");
            return "redirect:/admin/users/create";
        }
        try {
            userService.createUser(request);
            redirectAttributes.addFlashAttribute("success", "User " + request.getEmail() + " created successfully.");
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
        model.addAttribute("allRoles", Role.values());
        model.addAttribute("allStatuses", UserStatus.values());
        return "admin/edituser";
    }

    // POST /admin/users/{id}/update
    @PostMapping("/{id}/update")
    public String updateUser(@PathVariable Long id,
                             @Valid @ModelAttribute EditUserRequest request,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error",
                    result.getFieldError() != null ? result.getFieldError().getDefaultMessage() : "Validation error");
            return "redirect:/admin/users/" + id + "/edit";
        }
        try {
            userService.updateUser(id, request);
            redirectAttributes.addFlashAttribute("success", "User updated successfully.");
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
            redirectAttributes.addFlashAttribute("success", "User deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/users";
    }

    // POST /admin/users/{id}/reset-password
    @PostMapping("/{id}/reset-password")
    public String resetUserPassword(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.sendPasswordResetForUser(id);
            redirectAttributes.addFlashAttribute("success", "Password reset link sent.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/users/" + id + "/edit";
    }
}