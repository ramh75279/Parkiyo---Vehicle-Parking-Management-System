package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.service.DashboardService;
import com.parkiyo.parkiyo.service.UserService;          // ADD THIS IMPORT
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;  // ADD THIS IMPORT
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final DashboardService dashboardService;
    private final UserService userService;               // ADD THIS LINE

    // GET /admin/dashboard
    @GetMapping("/dashboard")
    public String adminDashboard(Model model, Authentication auth) {  // ADD "Authentication auth"
        model.addAttribute("stats", dashboardService.getAdminDashboardStats());
        model.addAttribute("recentEntries", dashboardService.getRecentEntries(10));
        model.addAttribute("recentPayments", dashboardService.getRecentPayments(5));
        model.addAttribute("slotSummary", dashboardService.getSlotOccupancySummary());
        model.addAttribute("currentUser", userService.getUserByEmail(auth.getName())); // ADD THIS LINE
        return "dashboard/dashboard-admin";
    }

    // GET /admin/system-status
    @GetMapping("/system-status")
    public String systemStatus(Model model) {
        model.addAttribute("systemHealth", dashboardService.getSystemHealth());
        return "admin/systemstatuspage";
    }

    // GET /admin/notifications
    @GetMapping("/notifications")
    public String adminNotifications(Model model) {
        model.addAttribute("notifications", dashboardService.getAdminNotifications());
        return "account/notification";
    }

    // GET /admin/settings
    @GetMapping("/settings")
    public String adminSettings() {
        return "redirect:/account/settings";
    }
}