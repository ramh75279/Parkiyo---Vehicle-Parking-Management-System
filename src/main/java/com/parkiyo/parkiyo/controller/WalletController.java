package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.model.WalletTransaction;
import com.parkiyo.parkiyo.service.WalletReceiptSubmissionService;
import com.parkiyo.parkiyo.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/wallet")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;
    private final WalletReceiptSubmissionService walletReceiptSubmissionService;

    @GetMapping("/overview")
    public String walletOverview(Authentication auth, Model model) {
        String email = auth.getName();

        Map<String, Object> overview = walletService.getWalletOverview(email);

        List<WalletTransaction> transactions = walletService.getTransactionHistory(email);

        // Calculate real totals from transaction history
        BigDecimal totalIn = transactions.stream()
                .filter(t -> "CREDIT".equals(t.getType()))
                .map(WalletTransaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalOut = transactions.stream()
                .filter(t -> "DEBIT".equals(t.getType()))
                .map(WalletTransaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("wallet", overview.get("wallet"));
        model.addAttribute("user", overview.get("user"));
        model.addAttribute("balance", overview.get("balance"));
        model.addAttribute("transactions", transactions);
        model.addAttribute("totalIn", totalIn);
        model.addAttribute("totalOut", totalOut);
        model.addAttribute("pendingReceiptSubmission",
                walletReceiptSubmissionService.getPendingForUser(email).orElse(null));

        return "payments/walletoverview";
    }

    @PostMapping("/submit-receipt")
    public String submitReceipt(@RequestParam BigDecimal amount,
                                @RequestParam(value = "paymentMethod", required = false) String paymentMethod,
                                @RequestParam("receipt") MultipartFile receipt,
                                Authentication auth,
                                RedirectAttributes redirectAttributes) {
        try {
            walletReceiptSubmissionService.submit(auth.getName(), amount, paymentMethod, receipt);
            redirectAttributes.addFlashAttribute("success",
                    "Receipt uploaded successfully. Your top-up will appear after an admin approves it.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/wallet/overview";
    }

    @GetMapping
    public String redirectToOverview() {
        return "redirect:/wallet/overview";
    }
}