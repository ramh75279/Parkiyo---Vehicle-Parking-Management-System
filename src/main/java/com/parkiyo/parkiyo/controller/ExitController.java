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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/exit")
@PreAuthorize("isAuthenticated()")
public class ExitController {

    private final ExitService exitService;
    private final ParkingService parkingService;
    private final UserService userService;

    @GetMapping
    public String exitPage(Authentication auth, Model model) {
        String email = auth.getName();
        model.addAttribute("activeRecords", parkingService.getActiveRecordsByUser(email));
        model.addAttribute("exitRequest", new ExitRequest());
        model.addAttribute("currentUser", userService.getUserByEmail(email));
        return "parking/exitvehicle";
    }

    @PostMapping("/process")
    public String processExit(@ModelAttribute ExitRequest request,
                                Authentication auth,
                                RedirectAttributes redirectAttributes) {
        try {
            if (request.getParkingRecordId() != null) {
                ParkingRecord record = parkingService.getActiveRecordForExit(request.getParkingRecordId(), auth.getName());
                if (record.getVehicle() != null && record.getVehicle().getLicensePlate() != null) {
                    request.setLicensePlate(record.getVehicle().getLicensePlate());
                }
            }
            exitService.processExit(request, auth.getName());
            redirectAttributes.addFlashAttribute("success", "Vehicle exited successfully!");
            return "redirect:/parking?success=true";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/exit";
        }
    }
}
