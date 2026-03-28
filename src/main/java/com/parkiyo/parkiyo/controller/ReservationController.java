package com.parkiyo.controller;

import com.parkiyo.dto.ReservationRequest;
import com.parkiyo.service.ReservationService;
import com.parkiyo.service.SlotService;
import com.parkiyo.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final SlotService slotService;
    private final VehicleService vehicleService;

    // GET /reservations
    @GetMapping("/reservations")
    public String reservations(Authentication auth, Model model) {
        model.addAttribute("reservations", reservationService.getUserReservations(auth.getName()));
        return "advancereservation";
    }

    // GET /reservation  (single active reservation view)
    @GetMapping("/reservation")
    public String activeReservation(Authentication auth, Model model) {
        model.addAttribute("reservation", reservationService.getActiveReservation(auth.getName()));
        return "advancereservation";
    }

    // GET /reservations/slot-selection
    @GetMapping("/reservations/slot-selection")
    public String slotSelection(@RequestParam(required = false) LocalDate date,
                                @RequestParam(required = false) String zone,
                                Authentication auth,
                                Model model) {
        model.addAttribute("availableSlots", slotService.getAvailableSlots(date, zone));
        model.addAttribute("userVehicles", vehicleService.getVehiclesByUser(auth.getName()));
        model.addAttribute("zones", slotService.getAllZones());
        model.addAttribute("selectedDate", date);
        return "slotselection";
    }

    // POST /reservations/create
    @PostMapping("/reservations/create")
    public String createReservation(@Valid @ModelAttribute ReservationRequest request,
                                    Authentication auth,
                                    RedirectAttributes redirectAttributes) {
        try {
            Long reservationId = reservationService.createReservation(request, auth.getName());
            redirectAttributes.addFlashAttribute("success", "Slot reserved! Proceed to payment.");
            return "redirect:/payments/pending/" + reservationId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/reservations/slot-selection";
        }
    }

    // POST /reservations/{id}/cancel
    @PostMapping("/reservations/{id}/cancel")
    public String cancelReservation(@PathVariable Long id,
                                    Authentication auth,
                                    RedirectAttributes redirectAttributes) {
        try {
            reservationService.cancelReservation(id, auth.getName());
            redirectAttributes.addFlashAttribute("success", "Reservation cancelled.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/reservations";
    }
}
