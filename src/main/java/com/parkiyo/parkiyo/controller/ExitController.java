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

    // ─── USER ────────────────────────────────────────────────────────────────

    // GET /exit
    @GetMapping("/exit")
    @PreAuthorize("isAuthenticated()")
    public String userExitPage(Authentication auth, Model model) {
        model.addAttribute("activeRecords", parkingService.getActiveRecordsByUser(auth.getName()));
        model.addAttribute("exitRequest", new ExitRequest());
        return "exitvehicle";
    }

    // POST /exit
    @PostMapping("/exit")
    @PreAuthorize("isAuthenticated()")
    public String processUserExit(@Valid @ModelAttribute ExitRequest request,
                                  Authentication auth,
                                  RedirectAttributes redirectAttributes) {
        try {
            Long recordId = exitService.processExit(request, auth.getName());
            redirectAttributes.addFlashAttribute("success", "Exit recorded. Proceed to payment.");
            return "redirect:/payments/pending/" + recordId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/exit";
        }
    }

    // ─── ADMIN ───────────────────────────────────────────────────────────────

    // GET /admin/exit
    @GetMapping("/admin/exit")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminExitPage(Model model) {
        model.addAttribute("activeRecords", parkingService.getAllActiveRecords());
        model.addAttribute("recentExits", exitService.getRecentExits(20));
        model.addAttribute("exitRequest", new ExitRequest());
        return "exitvehicle-admin";
    }

    // POST /admin/exit
    @PostMapping("/admin/exit")
    @PreAuthorize("hasRole('ADMIN')")
    public String processAdminExit(@Valid @ModelAttribute ExitRequest request,
                                   Authentication auth,
                                   RedirectAttributes redirectAttributes) {
        try {
            exitService.processExit(request, auth.getName());
            redirectAttributes.addFlashAttribute("success", "Exit processed successfully.");
            return "redirect:/admin/exit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/exit";
        }
    }
}
