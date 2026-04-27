package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.service.DashboardService;
import com.parkiyo.parkiyo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final DashboardService dashboardService;
    private final UserService userService;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model, Authentication auth) {
        model.addAttribute("stats", dashboardService.getAdminDashboardStats());
        model.addAttribute("recentEntries", dashboardService.getRecentEntries(10));
        model.addAttribute("recentPayments", dashboardService.getRecentPayments(5));
        model.addAttribute("slotSummary", dashboardService.getSlotOccupancySummary());
        model.addAttribute("adminNotifications", dashboardService.getAdminRecentNotifications(12));
        model.addAttribute("adminUnreadCount", dashboardService.getAdminUnreadNotificationCount());
        model.addAttribute("currentUser", userService.getUserByEmail(auth.getName()));

        return "dashboard/dashboard-admin";
    }

    @GetMapping("/system-status")
    public String systemStatus(Model model) {
        model.addAttribute("systemHealth", Map.of(
                "status", "healthy",
                "uptime", "99.8%",
                "cpu", "34%",
                "memory", "2.8 GB / 8 GB"
        ));
        return "admin/systemstatuspage";
    }

    @GetMapping("/notifications")
    public String adminNotifications(Model model) {
        model.addAttribute("notifications", dashboardService.getAdminRecentNotifications(20));
        return "account/notification";
    }

    @GetMapping({"/settings", "/account-settings"})
    public String redirectToAccountSettings() {
        return "redirect:/account/settings";
    }
}