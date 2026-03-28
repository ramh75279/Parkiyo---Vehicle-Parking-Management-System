package com.parkiyo.controller;

import com.parkiyo.service.ParkingService;
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
        return "parking";
    }

    // GET /parking/{id}  - record details
    @GetMapping("/parking/{id}")
    @PreAuthorize("isAuthenticated()")
    public String parkingRecordDetails(@PathVariable Long id,
                                       Authentication auth,
                                       Model model) {
        model.addAttribute("record", parkingService.getRecordByIdAndUser(id, auth.getName()));
        return "parkingrecorddetails";
    }

    // GET /parking/ticket  - printable ticket for current session
    @GetMapping("/parking/ticket")
    @PreAuthorize("isAuthenticated()")
    public String parkingTicket(@RequestParam Long recordId,
                                Authentication auth,
                                Model model) {
        model.addAttribute("ticket", parkingService.getTicket(recordId, auth.getName()));
        return "parkingticket";
    }
}
