package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.dto.ExitRequest;
import com.parkiyo.parkiyo.service.ExitService;
import com.parkiyo.parkiyo.service.ParkingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class ExitController {

    private final ExitService exitService;
    private final ParkingService parkingService;

    // ─── USER EXIT ────────────────────────────────────────────────────────────────

    @GetMapping("/exit")
    @PreAuthorize("isAuthenticated()")
    public String userExitPage(Authentication auth, Model model) {
        model.addAttribute("activeRecords", parkingService.getActiveRecordsByUser(auth.getName()));
        model.addAttribute("exitRequest", new ExitRequest());
        return "parking/exitvehicle";
    }

    @PostMapping("/exit")
    @PreAuthorize("isAuthenticated()")
    public String processUserExit(@Valid @ModelAttribute ExitRequest request,
                                  Authentication auth,
                                  RedirectAttributes redirectAttributes) {
        try {
            Long paymentId = exitService.processExit(request, auth.getName());

            redirectAttributes.addFlashAttribute("success",
                    "Vehicle " + request.getLicensePlate() + " exited successfully. Please proceed to payment.");

            return "redirect:/payments/pending/" + paymentId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Exit failed: " + e.getMessage());
            return "redirect:/exit";
        }
    }

    // ─── ADMIN EXIT ───────────────────────────────────────────────────────────────

    @GetMapping("/admin/exit")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminExitPage(Model model) {
        model.addAttribute("activeRecords", parkingService.getAllActiveRecords());
        model.addAttribute("recentExits", exitService.getRecentExits(20));
        model.addAttribute("exitRequest", new ExitRequest());
        return "parking/exitvehicle-admin";
    }

    @PostMapping("/admin/exit")
    @PreAuthorize("hasRole('ADMIN')")
    public String processAdminExit(@Valid @ModelAttribute ExitRequest request,
                                   Authentication auth,
                                   RedirectAttributes redirectAttributes) {
        try {
            Long paymentId = exitService.processExit(request, auth.getName());

            redirectAttributes.addFlashAttribute("success",
                    "Vehicle " + request.getLicensePlate() + " exit processed successfully.");

            return "redirect:/admin/exit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Exit failed: " + e.getMessage());
            return "redirect:/admin/exit";
        }
    }
}