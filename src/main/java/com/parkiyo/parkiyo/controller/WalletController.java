package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.dto.WalletTopUpRequest;
import com.parkiyo.parkiyo.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

        Object wallet = overview.get("wallet");
        Object user = overview.get("user");
        Object balance = overview.get("balance");

        // ⚠️ no Transaction class
        List<?> transactions = walletService.getTransactionHistory(email);

        // ✅ SAFE fallback (no class dependency)
        double totalIn = 0;
        double totalOut = 0;

        // 👉 If you DON'T have Transaction class, SKIP calculation safely
        // (or calculate in service instead)

        model.addAttribute("wallet", wallet);
        model.addAttribute("user", user);
        model.addAttribute("balance", balance);
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

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Wallet topped up successfully with $" + request.getAmount()
            );

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