package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.dto.EntryRequest;
import com.parkiyo.parkiyo.dto.EntryResponse;
import com.parkiyo.parkiyo.service.EntryService;
import com.parkiyo.parkiyo.service.SlotService;
import com.parkiyo.parkiyo.service.VehicleService;
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
public class EntryController {

    private final EntryService entryService;
    private final SlotService slotService;
    private final VehicleService vehicleService;

    // ==================== USER ENTRY ====================

    @GetMapping("/entry")
    @PreAuthorize("isAuthenticated()")
    public String userEntryPage(Authentication auth, Model model) {
        model.addAttribute("availableSlots", slotService.getAvailableSlots());
        model.addAttribute("availableSlotCount", slotService.getAvailableSlots().size());
        model.addAttribute("occupiedSlotCount", slotService.getSlotOverview().get("occupied"));
        model.addAttribute("userVehicles", vehicleService.getVehiclesByUser(auth.getName()));
        model.addAttribute("entryRequest", new EntryRequest());
        return "parking/entry";
    }

    @PostMapping("/entry")
    @PreAuthorize("isAuthenticated()")
    public String processUserEntry(@Valid @ModelAttribute EntryRequest request,
                                   Authentication auth,
                                   RedirectAttributes redirectAttributes) {
        try {
            EntryResponse response = entryService.processEntry(request);

            redirectAttributes.addFlashAttribute("success",
                    "Vehicle " + request.getLicensePlate() + " entered successfully.");

            return "redirect:/parking";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Entry failed: " + e.getMessage());
            return "redirect:/entry";
        }
    }

    // ==================== ADMIN ENTRY ====================

    @GetMapping("/admin/entry")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminEntryPage(Model model) {
        model.addAttribute("availableSlots", slotService.getAvailableSlots());
        model.addAttribute("availableSlotCount", slotService.getAvailableSlots().size());
        model.addAttribute("occupiedSlotCount", slotService.getSlotOverview().get("occupied"));

        // TODO: Implement getRecentEntries(int) method in EntryService later
        // model.addAttribute("recentEntries", entryService.getRecentEntries(20));

        model.addAttribute("entryRequest", new EntryRequest());
        return "parking/entry-admin";
    }

    @PostMapping("/admin/entry")
    @PreAuthorize("hasRole('ADMIN')")
    public String processAdminEntry(@Valid @ModelAttribute EntryRequest request,
                                    Authentication auth,
                                    RedirectAttributes redirectAttributes) {
        try {
            EntryResponse response = entryService.processEntry(request);

            redirectAttributes.addFlashAttribute("success",
                    "Vehicle " + request.getLicensePlate() + " entry logged successfully.");

            return "redirect:/admin/entry";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Entry failed: " + e.getMessage());
            return "redirect:/admin/entry";
        }
    }
}