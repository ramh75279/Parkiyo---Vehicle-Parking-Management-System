package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.service.ParkingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ParkingController {

    private final ParkingService parkingService;

    // GET /parking  - user's active and past parking sessions
    @GetMapping("/parking")
    @PreAuthorize("isAuthenticated()")
    public String parkingPage(Authentication auth, Model model) {
        model.addAttribute("activeRecords", parkingService.getActiveRecordsByUser(auth.getName()));
        model.addAttribute("pastRecords", parkingService.getPastRecordsByUser(auth.getName()));
        return "parking/parking";
    }

    @GetMapping("/admin/parking")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminParkingPage(Model model) {
        model.addAttribute("activeRecords", parkingService.getAllActiveRecords());
        model.addAttribute("pastRecords", parkingService.getAllPastRecords());
        return "parking/parking";
    }

    // GET /parking/{id}  - record details
    @GetMapping("/parking/{id}")
    @PreAuthorize("isAuthenticated()")
    public String parkingRecordDetails(@PathVariable Long id,
                                       Authentication auth,
                                       Model model) {
        model.addAttribute("record", parkingService.getRecordByIdAndUser(id, auth.getName()));
        return "parking/parkingrecorddetails";
    }

    // GET /parking/ticket  - printable ticket for current session
    @GetMapping("/parking/ticket")
    @PreAuthorize("isAuthenticated()")
    public String parkingTicket(@RequestParam Long recordId,
                                Authentication auth,
                                Model model) {
        model.addAttribute("ticket", parkingService.getTicket(recordId, auth.getName()));
        return "parking/parkingticket";
    }

    @GetMapping("/admin/tickets/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminParkingTicket(@PathVariable Long id, Model model) {
        model.addAttribute("ticket", parkingService.getAdminTicket(id));
        return "parking/parkingticket";
    }

    @GetMapping("/admin/parking/ticket")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminParkingTicketQuery(@RequestParam(required = false) Long recordId, Model model) {
        if (recordId == null) {
            return "redirect:/admin/parking";
        }
        model.addAttribute("ticket", parkingService.getAdminTicket(recordId));
        return "parking/parkingticket";
    }
}
