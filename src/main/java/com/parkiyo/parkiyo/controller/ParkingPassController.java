package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.service.PassPlanService;
import com.parkiyo.parkiyo.service.UserPassService;
import com.parkiyo.parkiyo.service.UserService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/passes")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class ParkingPassController {

    private final PassPlanService passPlanService;
    private final UserPassService userPassService;
    private final UserService userService;
    private final WalletService walletService;

    @GetMapping
    public String passesHome(Authentication auth, Model model) {
        String email = auth.getName();
        model.addAttribute("plans", passPlanService.listActiveCatalog());
        model.addAttribute("myPasses", userPassService.listForUser(email));
        model.addAttribute("activePass", userPassService.getCurrentActivePass(email).orElse(null));
        model.addAttribute("walletBalance", walletService.getBalance(email));
        model.addAttribute("currentUser", userService.getUserByEmail(email));
        return "passes/pass-catalog";
    }

    @PostMapping("/purchase")
    public String purchase(@RequestParam Long planId,
                           Authentication auth,
                           RedirectAttributes redirectAttributes) {
        try {
            userPassService.purchasePlan(auth.getName(), planId);
            redirectAttributes.addFlashAttribute("success", "Parking pass purchased and activated.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/passes";
    }

    @PostMapping("/cancel")
    public String cancel(Authentication auth, RedirectAttributes redirectAttributes) {
        try {
            userPassService.cancelOwnActivePass(auth.getName());
            redirectAttributes.addFlashAttribute("success", "Your active pass was cancelled.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/passes";
    }
}
