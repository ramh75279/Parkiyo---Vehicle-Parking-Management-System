package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.model.User;
import com.parkiyo.parkiyo.service.UserService;
import com.parkiyo.parkiyo.service.WalletReceiptSubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/wallet-receipts")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminWalletReceiptController {

    private final WalletReceiptSubmissionService walletReceiptSubmissionService;
    private final UserService userService;

    @GetMapping
    public String list(Authentication auth, Model model) {
        User admin = userService.getUserByEmail(auth.getName());
        model.addAttribute("currentUser", admin);
        model.addAttribute("pendingReceipts", walletReceiptSubmissionService.findPendingForAdmin());
        model.addAttribute("recentReceipts", walletReceiptSubmissionService.findRecentProcessedForAdmin());
        return "payments/wallet-receipts-admin";
    }

    @PostMapping("/{id}/approve")
    public String approve(@PathVariable Long id,
                          Authentication auth,
                          RedirectAttributes redirectAttributes) {
        try {
            walletReceiptSubmissionService.approve(id, auth.getName());
            redirectAttributes.addFlashAttribute("success", "Wallet top-up approved and credited.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/wallet-receipts";
    }

    @PostMapping("/{id}/reject")
    public String reject(@PathVariable Long id,
                         @RequestParam(value = "note", required = false) String note,
                         Authentication auth,
                         RedirectAttributes redirectAttributes) {
        try {
            walletReceiptSubmissionService.reject(id, auth.getName(), note);
            redirectAttributes.addFlashAttribute("success", "Request rejected. The user has been notified.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/wallet-receipts";
    }
}
