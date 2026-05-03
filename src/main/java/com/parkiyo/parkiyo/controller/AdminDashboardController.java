package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.service.DashboardService;
import com.parkiyo.parkiyo.service.NotificationService;
import com.parkiyo.parkiyo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    private final UserService userService;
    private final NotificationService notificationService;

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
        Runtime runtime = Runtime.getRuntime();
        long usedMemoryMB = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);
        long totalMemoryMB = runtime.totalMemory() / (1024 * 1024);

        model.addAttribute("usedMemoryMB", usedMemoryMB);
        model.addAttribute("totalMemoryMB", totalMemoryMB);
        model.addAttribute("availableProcessors", runtime.availableProcessors());
        model.addAttribute("totalUsers", userService.getTotalUsers());
        model.addAttribute("activeUsers", userService.countActiveUsers());
        return "admin/systemstatuspage";
    }

    @GetMapping("/notifications")
    public String adminNotifications(Model model, Authentication auth) {
        var notifications = notificationService.getUserNotifications(auth.getName());
        long unreadCount = notificationService.getUnreadCount(auth.getName());
        model.addAttribute("notifications", notifications);
        model.addAttribute("todayCount", unreadCount);
        return "account/notification";
    }

    @GetMapping({"/settings", "/account-settings"})
    public String redirectToAccountSettings() {
        return "redirect:/account/settings";
    }
}