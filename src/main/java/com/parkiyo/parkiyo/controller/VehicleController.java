package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.dto.VehicleRequest;
import com.parkiyo.parkiyo.model.ParkingRecord;
import com.parkiyo.parkiyo.model.Vehicle;
import com.parkiyo.parkiyo.service.VehicleService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/admin/vehicles")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    // GET /admin/vehicles
    @GetMapping
    public String vehicleList(@RequestParam(required = false) String search,
                              @RequestParam(required = false) String category,
                              @RequestParam(required = false) String status,
                              Model model) {

        List<Vehicle> vehicles = vehicleService.getAllVehicles(search, category);
        List<Map<String, Object>> vehicleRows = vehicles.stream().map(this::toVehicleRow).toList();
        if (status != null && !status.isBlank()) {
            vehicleRows = vehicleRows.stream().filter(v -> matchesStatus(v, status)).toList();
        }

        // Fixed: Convert to DTO/row inside the service transaction or force initialize
        model.addAttribute("vehicles", vehicleRows);
        model.addAttribute("categories", vehicleService.getAllCategories());
        model.addAttribute("stats", buildVehicleStats(vehicles));
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", 1);
        model.addAttribute("pageNumbers", List.of(1));

        return "vehicles/vehicle-list-page";
    }

    // ================== HELPER METHODS ==================

    private Map<String, Object> toVehicleRow(Vehicle vehicle) {
        Map<String, Object> row = new HashMap<>();

        row.put("id", vehicle.getId());
        row.put("plate", vehicle.getLicensePlate());

        String ownerName = "Guest";
        if (vehicle.getUser() != null) {
            try {
            ownerName = vehicle.getUser().getFirstName() + " " +
                (vehicle.getUser().getLastName() != null
                    ? vehicle.getUser().getLastName()
                    : "");
            } catch (Exception e) {
            ownerName = "User #" + vehicle.getUser().getId();
            }
        }

        row.put("ownerName", ownerName.trim());

        row.put("category",
            vehicle.getCategory() != null
                ? formatCategory(vehicle.getCategory().name())
                : "-");

        row.put("makeModel", buildMakeModel(vehicle.getMake(), vehicle.getModel()));

        row.put("color",
            vehicle.getColor() != null && !vehicle.getColor().isBlank()
                ? vehicle.getColor()
                : "-");

        row.put("colorHex", colorToHex(vehicle.getColor()));

        row.put("status", vehicle.isActive() ? "Active" : "Inactive");

        row.put("parkingStatus",
            vehicleService.isVehicleCurrentlyParked(vehicle.getId())
                ? "Parked"
                : "Not Parked");

        row.put("registeredOn",
            vehicle.getCreatedAt() != null
                ? vehicle.getCreatedAt().toLocalDate()
                .format(DateTimeFormatter.ISO_DATE)
                : "-");

        return row;
    }

        private boolean matchesStatus(Map<String, Object> row, String status) {
        if (status == null || status.isBlank()) {
            return true;
        }
        String requested = status.trim().toLowerCase(Locale.ROOT);
        String rowStatus = String.valueOf(row.getOrDefault("status", "")).toLowerCase(Locale.ROOT);
        String rowParkingStatus = String.valueOf(row.getOrDefault("parkingStatus", "")).toLowerCase(Locale.ROOT);
        return rowStatus.equals(requested) || rowParkingStatus.equals(requested);
        }

    @GetMapping("/export")
    @ResponseBody
    public ResponseEntity<String> exportVehiclesCsv() {

    List<Vehicle> vehicles = vehicleService.getAllVehicles(null, null);

    StringBuilder csv = new StringBuilder();
    csv.append("Plate,Owner,Category,Make,Model,Status\n");

    for (Vehicle vehicle : vehicles) {
        String owner = "Guest";
        if (vehicle.getUser() != null) {
            String firstName = vehicle.getUser().getFirstName() != null ? vehicle.getUser().getFirstName().trim() : "";
            String lastName = vehicle.getUser().getLastName() != null ? vehicle.getUser().getLastName().trim() : "";
            String fullName = (firstName + " " + lastName).trim();
            owner = fullName.isBlank() ? vehicle.getUser().getEmail() : fullName;
        }

        csv.append(csvSafe(vehicle.getLicensePlate())).append(",")
                .append(csvSafe(owner)).append(",")
                .append(csvSafe(vehicle.getCategory() != null ? formatCategory(vehicle.getCategory().name()) : "")).append(",")
                .append(csvSafe(vehicle.getMake())).append(",")
                .append(csvSafe(vehicle.getModel())).append(",")
                .append(vehicle.isActive() ? "Active" : "Inactive")
                .append("\n");
    }

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=vehicles.csv")
                .contentType(new MediaType("text", "csv"))
            .body(csv.toString());
    }

            private String csvSafe(String value) {
            if (value == null) {
                return "";
            }
            String escaped = value.replace("\"", "\"\"");
            return "\"" + escaped + "\"";
            }

    private Map<String, Object> buildVehicleStats(List<Vehicle> vehicles) {
            long activeCount = vehicles.stream().filter(Vehicle::isActive).count();
            long inactiveCount = vehicles.size() - activeCount;

            long parkedCount = vehicles.stream()
                .filter(v -> vehicleService.isVehicleCurrentlyParked(v.getId()))
                .count();

            Map<String, Object> stats = new HashMap<>();
            stats.put("total", vehicles.size());
            stats.put("parked", parkedCount);
            stats.put("active", activeCount);
            stats.put("inactive", inactiveCount);

            return stats;
    }

    private String buildMakeModel(String make, String model) {
        boolean hasMake = make != null && !make.isBlank();
        boolean hasModel = model != null && !model.isBlank();
        if (!hasMake && !hasModel) return "-";
        if (!hasMake) return model;
        if (!hasModel) return make;
        return make + " " + model;
    }

    private String formatCategory(String category) {
        if (category == null || category.isBlank()) return "-";
        return category.substring(0, 1).toUpperCase(Locale.ROOT)
                + category.substring(1).toLowerCase(Locale.ROOT);
    }

    private String colorToHex(String color) {
        if (color == null || color.isBlank()) {
            return "#94a3b8";
        }

        return switch (color.trim().toLowerCase(Locale.ROOT)) {
            case "white" -> "#f8fafc";
            case "black" -> "#0f172a";
            case "silver", "gray", "grey" -> "#94a3b8";
            case "blue" -> "#3b82f6";
            case "red" -> "#ef4444";
            case "green" -> "#22c55e";
            case "yellow" -> "#eab308";
            case "orange" -> "#f97316";
            case "brown" -> "#92400e";
            case "purple" -> "#a855f7";
            default -> "#94a3b8";
        };
    }

    // ================== OTHER METHODS (unchanged) ==================

    @GetMapping("/browse")
    public String browseByCategory(@RequestParam(required = false) String category, Model model) {
        model.addAttribute("categories", vehicleService.getAllCategories());
        model.addAttribute("vehicles", vehicleService.getVehiclesByCategory(category));
        model.addAttribute("selectedCategory", category);
        return "vehicles/browse-vehicle-by-category";
    }

    @GetMapping("/add")
    public String addVehiclePage(Model model) {
        model.addAttribute("vehicleRequest", new VehicleRequest());
        model.addAttribute("categories", vehicleService.getAllCategories());
        return "vehicles/add-vehicle-page";
    }

    @PostMapping("/create")
    public String createVehicle(@Valid @ModelAttribute VehicleRequest request,
                                RedirectAttributes redirectAttributes) {
        try {
            vehicleService.createVehicle(request);
            redirectAttributes.addFlashAttribute("success", "Vehicle added successfully.");
            return "redirect:/admin/vehicles";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/vehicles/add";
        }
    }

    @GetMapping("/{id}")
    public String vehicleDetails(@PathVariable Long id, Model model) {
        Vehicle vehicle = vehicleService.getVehicleByIdForView(id);
        List<ParkingRecord> parkingHistory = vehicleService.getVehicleParkingHistory(id);
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("parkingHistory", parkingHistory);

        ParkingRecord activeSession = parkingHistory.stream()
                .filter(ParkingRecord::isActive)
                .findFirst()
                .orElse(null);
        model.addAttribute("activeSession", activeSession);

        int totalVisits = parkingHistory.size();
        BigDecimal totalPaid = parkingHistory.stream()
                .filter(ParkingRecord::isPaid)
                .map(pr -> pr.getAmountCharged() != null ? pr.getAmountCharged() : pr.getAmount())
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        long unpaidCount = parkingHistory.stream().filter(pr -> !pr.isPaid()).count();
        int avgMins = (int) parkingHistory.stream()
                .filter(pr -> pr.getDurationMinutes() != null && pr.getDurationMinutes() > 0)
                .mapToInt(ParkingRecord::getDurationMinutes)
                .average()
                .orElse(0.0);

        model.addAttribute("totalVisits", totalVisits);
        model.addAttribute("totalPaidSum", totalPaid);
        model.addAttribute("unpaidCount", unpaidCount);
        model.addAttribute("avgDurationLabel", formatMinutesShort(avgMins));

        if (activeSession != null && activeSession.getEntryTime() != null) {
            long runMins = ChronoUnit.MINUTES.between(activeSession.getEntryTime(), LocalDateTime.now());
            model.addAttribute("activeDurationLabel", formatMinutesShort((int) Math.max(0, runMins)));
        } else {
            model.addAttribute("activeDurationLabel", "—");
        }

        return "vehicles/vehicle-details-page";
    }

    private static String formatMinutesShort(int mins) {
        if (mins <= 0) {
            return "—";
        }
        int h = mins / 60;
        int m = mins % 60;
        if (h == 0) {
            return m + "m";
        }
        return h + "h " + m + "m";
    }

    @GetMapping("/{id}/edit")
    public String editVehiclePage(@PathVariable Long id, Model model) {
        model.addAttribute("vehicle", vehicleService.getVehicleById(id));
        model.addAttribute("categories", vehicleService.getAllCategories());
        return "vehicles/edit-vehicle-page";
    }

    @PostMapping("/{id}/update")
    public String updateVehicle(@PathVariable Long id,
                                @Valid @ModelAttribute VehicleRequest request,
                                RedirectAttributes redirectAttributes) {
        try {
            vehicleService.updateVehicle(id, request);
            redirectAttributes.addFlashAttribute("success", "Vehicle updated.");
            return "redirect:/admin/vehicles/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/vehicles/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteVehicle(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            vehicleService.deleteVehicle(id);
            redirectAttributes.addFlashAttribute("success", "Vehicle deactivated.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/vehicles";
    }

    @GetMapping("/quick-register")
    public String quickRegisterPage() {
        return "vehicles/quick-register-by-plate";
    }

    @PostMapping("/quick-register")
    public String quickRegister(@RequestParam String licensePlate,
                                @RequestParam(required = false) String category,
                                RedirectAttributes redirectAttributes) {
        try {
            vehicleService.quickRegisterByPlate(licensePlate, category);
            redirectAttributes.addFlashAttribute("success", "Vehicle " + licensePlate + " registered.");
            return "redirect:/admin/vehicles";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/vehicles/quick-register";
        }
    }

    // Import methods remain unchanged...
    @GetMapping("/import")
    public String importPage(HttpSession session, Model model) {
        model.addAllAttributes(vehicleService.getImportPreview(session));
        return "vehicles/vehicle-import-page";
    }

    @GetMapping("/import/template")
    @ResponseBody
    public ResponseEntity<String> importTemplate() {
        String csv = "licensePlate,category,make,model,color,year,ownerEmail\n"
                + "ABC-1234,CAR,Toyota,Axio,White,2021,user@parkiyo.com\n";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=parkiyo-vehicle-import-template.csv")
                .contentType(new MediaType("text", "csv"))
                .body(csv);
    }

    @PostMapping("/import/upload")
    public String uploadImport(@RequestParam("file") MultipartFile file,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        try {
            vehicleService.uploadImportFile(file, session);
            redirectAttributes.addFlashAttribute("success", "File uploaded. Review and confirm.");
            return "redirect:/admin/vehicles/import";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/vehicles/import";
        }
    }

    @PostMapping("/import/confirm")
    public String confirmImport(@RequestParam(defaultValue = "false") boolean skipDuplicates,
                                @RequestParam(defaultValue = "false") boolean importWithWarnings,
                                @RequestParam(defaultValue = "false") boolean setAllActive,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        try {
            int count = vehicleService.confirmImport(session, skipDuplicates, importWithWarnings, setAllActive);
            redirectAttributes.addFlashAttribute("success", count + " vehicles imported.");
            return "redirect:/admin/vehicles";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/vehicles/import";
        }
    }

    @PostMapping("/import/remove")
    public String removeImport(HttpSession session, RedirectAttributes redirectAttributes) {
        vehicleService.clearPendingImport(session);
        redirectAttributes.addFlashAttribute("success", "Import file removed.");
        return "redirect:/admin/vehicles/import";
    }
}