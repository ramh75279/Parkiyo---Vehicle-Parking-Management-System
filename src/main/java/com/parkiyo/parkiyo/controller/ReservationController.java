package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.dto.ReservationRequest;
import com.parkiyo.parkiyo.enums.ReservationStatus;
import com.parkiyo.parkiyo.enums.SlotStatus;
import com.parkiyo.parkiyo.enums.VehicleCategory;
import com.parkiyo.parkiyo.model.ParkingSlot;
import com.parkiyo.parkiyo.model.Reservation;
import com.parkiyo.parkiyo.model.Vehicle;
import com.parkiyo.parkiyo.service.ReservationService;
import com.parkiyo.parkiyo.service.SlotService;
import com.parkiyo.parkiyo.service.UserService;
import com.parkiyo.parkiyo.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

@Controller
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final SlotService slotService;
    private final VehicleService vehicleService;
    private final UserService userService;

    /**
     * Main Advance Reservations Page - Lists all user reservations
     */
    @GetMapping("/reservations")
    public String reservations(Authentication auth, Model model) {
        populateReservationPage(auth.getName(), model);
        return "parking/advancereservation";
    }

    /**
     * Legacy path used by sidebar links — same hub as {@code /reservations}.
     */
    @GetMapping("/reservation")
    public String activeReservation() {
        return "redirect:/reservations";
    }

    private void populateReservationPage(String email, Model model) {
        List<Reservation> reservations = reservationService.getUserReservations(email);
        model.addAttribute("reservations", reservations != null ? reservations : Collections.emptyList());
        model.addAttribute("currentUser", userService.getUserByEmail(email));
        model.addAttribute("kpiUpcomingWeek", reservationService.countConfirmedStartingWithinSevenDays(email));
        model.addAttribute("kpiToday", reservationService.countTodayReservations(email));
        model.addAttribute("kpiMonthBookings", reservationService.countBookingsStartingThisMonth(email));
        model.addAttribute("kpiMonthCancellations", reservationService.countCancellationsThisMonth(email));
        model.addAttribute("zones", slotService.getAllZones());
        model.addAttribute("avgHourlyRate", slotService.getAverageHourlyRateForEstimate());
    }

    // ====================== SLOT SELECTION FOR NEW RESERVATION ======================
    @GetMapping("/reservations/slot-selection")
    public String slotSelection(@RequestParam(required = false) LocalDate date,
                                @RequestParam(required = false) String zone,
                                @RequestParam(required = false) String plate,
                                @RequestParam(required = false) String arrival,
                                @RequestParam(required = false) String departure,
                                @RequestParam(required = false) String vehicleCategory,
                                Authentication auth,
                                Model model) {
        LocalDate effectiveDate = date != null ? date : LocalDate.now();
        LocalDate dayForTimes = date != null ? date : effectiveDate;
        String trimmedPlate = plate != null ? plate.trim() : null;

        List<Vehicle> userVehicles = vehicleService.getVehiclesByUser(auth.getName());
        Long prefVehicleId = null;
        if (trimmedPlate != null) {
            for (Vehicle v : userVehicles) {
                if (trimmedPlate.equalsIgnoreCase(v.getLicensePlate())) {
                    prefVehicleId = v.getId();
                    break;
                }
            }
        }

        VehicleCategory prefCategoryParam = parseVehicleCategoryQuery(vehicleCategory);
        VehicleCategory summaryType = prefCategoryParam;
        if (prefVehicleId != null) {
            for (Vehicle v : userVehicles) {
                if (prefVehicleId.equals(v.getId())) {
                    summaryType = v.getCategory();
                    break;
                }
            }
        }
        String summaryVehicleTypeLabel = summaryType != null ? formatVehicleTypeLabel(summaryType) : null;

        String defaultVehicleCategory = "CAR";
        if (summaryType != null) {
            defaultVehicleCategory = summaryType.name();
        }

        List<ParkingSlot> allSlots = slotService.findAllSlotsForSlotSelection();
        model.addAttribute("slotsForPicker", buildSlotPickerRowsWithStatus(allSlots));
        TreeSet<String> zoneSet = new TreeSet<>(slotService.getAllZones());
        for (ParkingSlot s : allSlots) {
            if (s.getZone() != null && !s.getZone().isBlank()) {
                zoneSet.add(s.getZone());
            }
        }
        if (zoneSet.isEmpty()) {
            zoneSet.add("A");
        }
        model.addAttribute("zones", new ArrayList<>(zoneSet));
        String initialZone = null;
        if (zone != null && !zone.isBlank()) {
            String rz = zone.trim();
            initialZone = zoneSet.stream().filter(z -> z.equalsIgnoreCase(rz)).findFirst().orElse(null);
        }
        model.addAttribute("initialZone", initialZone);

        BigDecimal hourly = slotService.getAverageHourlyRateForEstimate();
        model.addAttribute("avgRateLabel", "Standard · Rs. " + hourly + "/hr");
        model.addAttribute("avgHourlyRate", hourly);
        model.addAttribute("userVehicles", userVehicles);
        model.addAttribute("defaultVehicleCategory", defaultVehicleCategory);
        model.addAttribute("summaryVehicleTypeLabel", summaryVehicleTypeLabel);
        model.addAttribute("selectedDate", effectiveDate);
        model.addAttribute("prefPlate", trimmedPlate);
        model.addAttribute("prefVehicleId", prefVehicleId);

        DateTimeFormatter dtFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        DateTimeFormatter dayFmt = DateTimeFormatter.ofPattern("d MMM uuuu", Locale.ENGLISH);
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);

        LocalDateTime startLdt = null;
        LocalDateTime endLdt = null;
        try {
            if (arrival != null && !arrival.isBlank()) {
                startLdt = LocalDateTime.of(dayForTimes, parseFlexibleTime(arrival));
            }
            if (departure != null && !departure.isBlank()) {
                endLdt = LocalDateTime.of(dayForTimes, parseFlexibleTime(departure));
            }
            if (startLdt != null && endLdt != null && !endLdt.isAfter(startLdt)) {
                endLdt = endLdt.plusDays(1);
            }
        } catch (Exception ignored) {
            startLdt = null;
            endLdt = null;
        }

        String prefStart = startLdt != null ? startLdt.format(dtFmt) : null;
        String prefEnd = endLdt != null ? endLdt.format(dtFmt) : null;
        model.addAttribute("prefStart", prefStart);
        model.addAttribute("prefEnd", prefEnd);

        long billableHours = 0;
        BigDecimal estFee = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        if (startLdt != null && endLdt != null) {
            long diffMin = Duration.between(startLdt, endLdt).toMinutes();
            if (diffMin > 0) {
                billableHours = (long) Math.ceil(diffMin / 60.0);
                estFee = hourly.multiply(BigDecimal.valueOf(billableHours)).setScale(2, RoundingMode.HALF_UP);
            }
        }

        String summaryDuration = billableHours > 0
                ? (billableHours + " hour" + (billableHours == 1 ? "" : "s"))
                : "—";
        String feeSub = billableHours > 0
                ? (billableHours + "h × Rs. " + hourly + "/hr")
                : "Based on typical slot rate";
        String feeTotal = billableHours > 0 ? ("Rs. " + estFee) : "Rs. 0.00";

        String plateUpper = trimmedPlate != null ? trimmedPlate.toUpperCase(Locale.ROOT) : "—";
        String slotHeaderSubtitle = "Make a new reservation";
        if (trimmedPlate != null && startLdt != null && endLdt != null) {
            slotHeaderSubtitle = plateUpper + " · " + effectiveDate.format(dayFmt) + " · "
                    + startLdt.format(timeFmt) + " – " + endLdt.format(timeFmt);
        } else if (trimmedPlate != null) {
            slotHeaderSubtitle = plateUpper + " · " + effectiveDate.format(dayFmt);
        } else if (startLdt != null && endLdt != null) {
            slotHeaderSubtitle = effectiveDate.format(dayFmt) + " · "
                    + startLdt.format(timeFmt) + " – " + endLdt.format(timeFmt);
        }

        model.addAttribute("slotHeaderSubtitle", slotHeaderSubtitle);
        model.addAttribute("summaryPlateDisplay", plateUpper);
        model.addAttribute("summaryDateFormatted", effectiveDate.format(dayFmt));
        model.addAttribute("summaryArrivalFormatted", startLdt != null ? startLdt.format(timeFmt) : "—");
        model.addAttribute("summaryDepartureFormatted", endLdt != null ? endLdt.format(timeFmt) : "—");
        model.addAttribute("summaryDurationLabel", summaryDuration);
        model.addAttribute("summaryFeeTotal", feeTotal);
        model.addAttribute("summaryFeeSubtext", feeSub);
        boolean showTimesReadOnly = prefStart != null && prefEnd != null;
        model.addAttribute("showTimesReadOnly", showTimesReadOnly);
        model.addAttribute("usePlateAndTypeOnly", trimmedPlate != null && prefVehicleId == null);

        return "slots/slotselection";
    }

    private static VehicleCategory parseVehicleCategoryQuery(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            return VehicleCategory.valueOf(raw.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static String formatVehicleTypeLabel(VehicleCategory c) {
        return switch (c) {
            case CAR -> "Car";
            case MOTORCYCLE -> "Bike";
            case VAN -> "Van";
            case TRUCK -> "Truck";
        };
    }

    private static String mapSlotUiStatus(SlotStatus st) {
        if (st == null) {
            return "occupied";
        }
        return switch (st) {
            case AVAILABLE -> "available";
            case OCCUPIED -> "occupied";
            case RESERVED, MAINTENANCE -> "reserved";
        };
    }

    private static List<Map<String, Object>> buildSlotPickerRowsWithStatus(List<ParkingSlot> slots) {
        List<Map<String, Object>> rows = new ArrayList<>();
        for (ParkingSlot s : slots) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("id", s.getId());
            row.put("code", s.getSlotNumber());
            String z = s.getZone();
            row.put("zone", z != null && !z.isBlank() ? z : "A");
            row.put("s", mapSlotUiStatus(s.getStatus()));
            rows.add(row);
        }
        return rows;
    }

    private static LocalTime parseFlexibleTime(String raw) {
        String s = raw.trim();
        if (s.length() >= 5) {
            s = s.substring(0, 5);
        }
        return LocalTime.parse(s);
    }

    // ====================== CREATE RESERVATION ======================
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

    // ====================== EDIT RESERVATION ======================
    @GetMapping("/reservations/{id}/edit")
    public String editReservationForm(@PathVariable Long id,
                                      Authentication auth,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {
        try {
            Reservation reservation = reservationService.getReservationByIdForUser(id, auth.getName());

            if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
                redirectAttributes.addFlashAttribute("error", "Only upcoming (CONFIRMED) reservations can be edited.");
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

    // ====================== CANCEL RESERVATION ======================
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