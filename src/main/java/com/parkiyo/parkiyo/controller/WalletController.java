package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.dto.WalletTopUpRequest;
import com.parkiyo.parkiyo.model.WalletTransaction;
import com.parkiyo.parkiyo.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

        return "payments/walletoverview";
    }

    @PostMapping("/topup")
    public String topUp(@Valid @ModelAttribute WalletTopUpRequest request,
                        Authentication auth,
                        RedirectAttributes redirectAttributes) {
        try {
            walletService.topUp(auth.getName(), request.getAmount());
            redirectAttributes.addFlashAttribute("success",
                    "Wallet topped up successfully with Rs. " + request.getAmount());
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