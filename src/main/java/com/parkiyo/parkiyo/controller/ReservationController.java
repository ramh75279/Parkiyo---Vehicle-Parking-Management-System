package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.dto.ReservationRequest;
import com.parkiyo.parkiyo.enums.ReservationStatus;
import com.parkiyo.parkiyo.model.Reservation;
import com.parkiyo.parkiyo.service.ReservationService;
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

import java.time.LocalDate;
import java.util.Collections;

@Controller
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final SlotService slotService;
    private final VehicleService vehicleService;

    /**
     * Main Advance Reservations Page
     */
    @GetMapping("/reservations")
    public String reservations(Authentication auth, Model model) {
        String email = auth.getName();

        model.addAttribute("reservations", reservationService.getUserReservations(email));
        model.addAttribute("upcomingCount", reservationService.countUpcomingReservations(email));
        model.addAttribute("todayCount", reservationService.countTodayReservations(email));
        model.addAttribute("cancelledCount", reservationService.countCancelledReservations(email));
        model.addAttribute("completedCount", reservationService.countCompletedReservations(email));

        return "parking/advancereservation";
    }

    /**
     * Active Reservation View
     */
    @GetMapping("/reservation")
    public String activeReservation(Authentication auth, Model model) {
        String email = auth.getName();
        model.addAttribute("reservation", reservationService.getActiveReservation(email));
        // Safety for template
        model.addAttribute("reservations", Collections.emptyList());

        return "parking/advancereservation";
    }

    // ====================== SLOT SELECTION ======================
    @GetMapping("/reservations/slot-selection")
    public String slotSelection(@RequestParam(required = false) LocalDate date,
                                @RequestParam(required = false) String zone,
                                Authentication auth,
                                Model model) {
        model.addAttribute("availableSlots", slotService.getAvailableSlots(date, zone));
        model.addAttribute("userVehicles", vehicleService.getVehiclesByUser(auth.getName()));
        model.addAttribute("zones", slotService.getAllZones());
        model.addAttribute("selectedDate", date != null ? date : LocalDate.now());

        return "slots/slotselection";
    }

    // ====================== CREATE ======================
    @PostMapping("/reservations/create")
    public String createReservation(@Valid @ModelAttribute ReservationRequest request,
                                    Authentication auth,
                                    RedirectAttributes redirectAttributes) {
        try {
            Long paymentId = reservationService.createReservation(request, auth.getName());
            redirectAttributes.addFlashAttribute("success",
                    "Reservation created successfully! Please proceed to payment.");
            return "redirect:/payments/pending/" + paymentId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/reservations/slot-selection";
        }
    }

    // ====================== EDIT ======================
    @GetMapping("/reservations/{id}/edit")
    public String editReservationForm(@PathVariable Long id,
                                      Authentication auth,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {
        try {
            Reservation reservation = reservationService.getReservationByIdForUser(id, auth.getName());

            if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
                redirectAttributes.addFlashAttribute("error", "Only upcoming reservations can be edited.");
                return "redirect:/reservations";
            }

            model.addAttribute("reservation", reservation);
            model.addAttribute("userVehicles", vehicleService.getVehiclesByUser(auth.getName()));
            model.addAttribute("availableSlots", slotService.getAvailableSlots(
                    reservation.getStartTime().toLocalDate(), null));
            model.addAttribute("zones", slotService.getAllZones());

            return "parking/editreservation";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/reservations";
        }
    }

    @PostMapping("/reservations/{id}/edit")
    public String updateReservation(@PathVariable Long id,
                                    @Valid @ModelAttribute ReservationRequest request,
                                    Authentication auth,
                                    RedirectAttributes redirectAttributes) {
        try {
            reservationService.updateReservation(id, request, auth.getName());
            redirectAttributes.addFlashAttribute("success", "Reservation updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/reservations";
    }

    // ====================== CANCEL ======================
    @PostMapping("/reservations/{id}/cancel")
    public String cancelReservation(@PathVariable Long id,
                                    Authentication auth,
                                    RedirectAttributes redirectAttributes) {
        try {
            reservationService.cancelReservation(id, auth.getName());
            redirectAttributes.addFlashAttribute("success", "Reservation cancelled successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/reservations";
    }
}