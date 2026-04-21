package com.parkiyo.parkiyo.controller;

import com.parkiyo.dto.WalletTopUpRequest;
import com.parkiyo.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    // GET /wallet
    @GetMapping("/wallet")
    public String walletOverview(Authentication auth, Model model) {
        model.addAttribute("wallet", walletService.getWalletOverview(auth.getName()));
        model.addAttribute("transactions", walletService.getTransactionHistory(auth.getName()));
        return "walletoverview";
    }

    // POST /wallet/topup
    @PostMapping("/wallet/topup")
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
