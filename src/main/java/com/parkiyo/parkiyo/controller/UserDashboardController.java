package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.service.DashboardService;
import com.parkiyo.parkiyo.service.NotificationService;
import com.parkiyo.parkiyo.service.UserService;
import com.parkiyo.parkiyo.service.WalletService;
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
    private final UserService userService; // added

    // GET /dashboard/user
    @GetMapping("/dashboard/user")
    public String userDashboard(Authentication auth, Model model) {
        String email = auth.getName();
        model.addAttribute("currentUser", userService.getUserByEmail(email)); // added
        model.addAttribute("recentParking", dashboardService.getUserRecentParking(email, 5));
        model.addAttribute("activeReservation", dashboardService.getUserActiveReservation(email));
        model.addAttribute("walletBalance", walletService.getBalance(email));
        model.addAttribute("pendingPayments", dashboardService.getUserPendingPayments(email));
        return "dashboard/dashboard-user";
    }

    // GET /notifications (user)
    @GetMapping("/notifications")
    public String userNotifications(Authentication auth, Model model) {
        String email = auth.getName();
        model.addAttribute("currentUser", userService.getUserByEmail(email)); // added
        model.addAttribute("notifications", notificationService.getUserNotifications(email));
        return "account/notification-user";
    }

    // GET /assistant
    @GetMapping("/assistant")
    public String assistant(Authentication auth, Model model) {
        model.addAttribute("currentUser", userService.getUserByEmail(auth.getName())); // added
        return "assistant/assistant";
    }
}