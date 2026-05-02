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

@Controller
@RequestMapping("/wallet")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @GetMapping
    public String walletOverview(Authentication auth, Model model) {
        String email = auth.getName();

        var overview = walletService.getWalletOverview(email);

        model.addAttribute("wallet", overview.get("wallet"));
        model.addAttribute("user", overview.get("user"));
        model.addAttribute("balance", overview.get("balance"));
        model.addAttribute("transactions", walletService.getTransactionHistory(email));

        return "payments/walletoverview";
    }

    @PostMapping("/topup")
    public String topUp(@Valid @ModelAttribute WalletTopUpRequest request,
                        Authentication auth,
                        RedirectAttributes redirectAttributes) {
        try {
            walletService.topUp(auth.getName(), request.getAmount());
            redirectAttributes.addFlashAttribute("success",
                    "Wallet topped up successfully with $" + request.getAmount());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/wallet";
    }
}