package com.parkiyo.controller;

import com.parkiyo.service.DashboardService;
import com.parkiyo.service.NotificationService;
import com.parkiyo.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class UserDashboardController {

    private final DashboardService dashboardService;
    private final NotificationService notificationService;
    private final WalletService walletService;

    // GET /dashboard/user
    @GetMapping("/dashboard/user")
    public String userDashboard(Authentication auth, Model model) {
        String email = auth.getName();
        model.addAttribute("recentParking", dashboardService.getUserRecentParking(email, 5));
        model.addAttribute("activeReservation", dashboardService.getUserActiveReservation(email));
        model.addAttribute("walletBalance", walletService.getBalance(email));
        model.addAttribute("pendingPayments", dashboardService.getUserPendingPayments(email));
        return "dashboard-user";
    }

    // GET /notifications  (user)
    @GetMapping("/notifications")
    public String userNotifications(Authentication auth, Model model) {
        model.addAttribute("notifications",
                notificationService.getUserNotifications(auth.getName()));
        return "notification-user";
    }

    // GET /assistant
    @GetMapping("/assistant")
    public String assistant() {
        return "assistant";
    }
}
