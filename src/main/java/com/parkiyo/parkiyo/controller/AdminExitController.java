package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.dto.ExitRequest;
import com.parkiyo.parkiyo.model.ParkingRecord;
import com.parkiyo.parkiyo.service.ExitService;
import com.parkiyo.parkiyo.service.ParkingService;
import com.parkiyo.parkiyo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AdminExitController {

    private final ParkingService parkingService;
    private final ExitService exitService;
    private final UserService userService;

    @GetMapping("/admin/exit")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminExitPage(Authentication auth, Model model) {
        model.addAttribute("activeRecords", parkingService.getAllActiveRecords());
        model.addAttribute("exitRequest", new ExitRequest());
        model.addAttribute("currentUser", userService.getUserByEmail(auth.getName()));
        return "parking/exitvehicle-admin";
    }

    @PostMapping("/admin/exit")
    @PreAuthorize("hasRole('ADMIN')")
    public String processAdminExit(@ModelAttribute ExitRequest request,
                                     Authentication auth,
                                     RedirectAttributes redirectAttributes) {
        try {
            if (request.getParkingRecordId() != null) {
                ParkingRecord record = parkingService.getRecordById(request.getParkingRecordId());
                if (record.getVehicle() != null && record.getVehicle().getLicensePlate() != null) {
                    request.setLicensePlate(record.getVehicle().getLicensePlate());
                }
            }
            exitService.processExit(request, auth.getName());
            redirectAttributes.addFlashAttribute("success", "Vehicle exited successfully.");
            return "redirect:/admin/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/exit";
        }
    }
}
