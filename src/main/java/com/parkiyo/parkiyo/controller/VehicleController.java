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
import java.util.Optional;

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
                              Model model) {
        List<Vehicle> vehicles = vehicleService.getAllVehicles(search, category);
        model.addAttribute("vehicles", vehicles.stream().map(this::toVehicleRow).toList());
        model.addAttribute("categories", vehicleService.getAllCategories());
        model.addAttribute("stats", buildVehicleStats(vehicles));
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", 1);
        model.addAttribute("pageNumbers", List.of(1));
        return "vehicles/vehicle-list-page";
    }

    // GET /admin/vehicles/browse
    @GetMapping("/browse")
    public String browseByCategory(@RequestParam(required = false) String category, Model model) {
        model.addAttribute("categories", vehicleService.getAllCategories());
        model.addAttribute("vehicles", vehicleService.getVehiclesByCategory(category));
        model.addAttribute("selectedCategory", category);
        return "vehicles/browse-vehicle-by-category";
    }

    // GET /admin/vehicles/add
    @GetMapping("/add")
    public String addVehiclePage(Model model) {
        model.addAttribute("vehicleRequest", new VehicleRequest());
        model.addAttribute("categories", vehicleService.getAllCategories());
        return "vehicles/add-vehicle-page";
    }

    // POST /admin/vehicles/create
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

    // GET /admin/vehicles/{id}
    @GetMapping("/{id}")
    public String vehicleDetails(@PathVariable Long id, Model model) {
        Vehicle vehicle = vehicleService.getVehicleById(id);
        List<ParkingRecord> parkingHistory = vehicleService.getVehicleParkingHistory(id);

        Optional<ParkingRecord> activeOpt = parkingHistory.stream()
                .filter(r -> r.getExitTime() == null)
                .findFirst();

        BigDecimal totalPaid = parkingHistory.stream()
                .map(ParkingRecord::getAmountCharged)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int visitCount = parkingHistory.size();
        int avgMinutes = (int) Math.round(parkingHistory.stream()
                .map(ParkingRecord::getDurationMinutes)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0));

        long unpaidApprox = parkingHistory.stream()
                .filter(r -> r.getExitTime() == null && r.getPayment() == null)
                .count();

        Long activeSessionMinutes = activeOpt
                .map(a -> ChronoUnit.MINUTES.between(a.getEntryTime(), LocalDateTime.now()))
                .orElse(null);

        String activeSlotLabel = activeOpt
                .map(ParkingRecord::getSlot)
                .map(slot -> {
                    String num = slot.getSlotNumber() != null ? slot.getSlotNumber() : "";
                    String zone = slot.getZone();
                    if (zone != null && !zone.isBlank()) {
                        return "Slot " + num + " · " + zone;
                    }
                    return "Slot " + num;
                })
                .orElse("");

        model.addAttribute("vehicle", vehicle);
        model.addAttribute("parkingHistory", parkingHistory);
        model.addAttribute("activeParkingRecord", activeOpt.orElse(null));
        model.addAttribute("makeModel", buildMakeModel(vehicle.getMake(), vehicle.getModel()));
        model.addAttribute("categoryLabel", vehicle.getCategory() != null
                ? formatCategory(vehicle.getCategory().name())
                : "-");
        model.addAttribute("ownerDisplayName", resolveOwnerDisplayName(vehicle));
        model.addAttribute("visitCount", visitCount);
        model.addAttribute("totalPaidAmount", totalPaid);
        model.addAttribute("avgDurationLabel", formatDurationMinutes(avgMinutes));
        model.addAttribute("unpaidApproxCount", unpaidApprox);
        model.addAttribute("activeSessionMinutes", activeSessionMinutes);
        model.addAttribute("activeSlotLabel", activeSlotLabel);
        return "vehicles/vehicle-details-page";
    }

    // GET /admin/vehicles/{id}/edit
    @GetMapping("/{id}/edit")
    public String editVehiclePage(@PathVariable Long id, Model model) {
        model.addAttribute("vehicle", vehicleService.getVehicleById(id));
        model.addAttribute("categories", vehicleService.getAllCategories());
        return "vehicles/edit-vehicle-page";
    }

    // POST /admin/vehicles/{id}/update
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

    // POST /admin/vehicles/{id}/delete
    @PostMapping("/{id}/delete")
    public String deleteVehicle(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            vehicleService.deleteVehicle(id);
            redirectAttributes.addFlashAttribute("success", "Vehicle deleted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/vehicles";
    }

    // GET /admin/vehicles/quick-register
    @GetMapping("/quick-register")
    public String quickRegisterPage() {
        return "vehicles/quick-register-by-plate";
    }

    @GetMapping("/quick-register/step1")
    public String quickRegisterStepOne() {
        return "redirect:/admin/vehicles/quick-register";
    }

    // POST /admin/vehicles/quick-register
    @PostMapping("/quick-register")
    public String quickRegister(@RequestParam String licensePlate,
                                @RequestParam(required = false) String category,
                                RedirectAttributes redirectAttributes) {
        try {
            vehicleService.quickRegisterByPlate(licensePlate, category);
            redirectAttributes.addFlashAttribute("success",
                    "Vehicle " + licensePlate + " registered.");
            return "redirect:/admin/vehicles";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/vehicles/quick-register";
        }
    }

    // GET /admin/vehicles/import
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

    // POST /admin/vehicles/import/upload
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

    // POST /admin/vehicles/import/confirm
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

    // POST /admin/vehicles/import/remove
    @PostMapping("/import/remove")
    public String removeImport(HttpSession session, RedirectAttributes redirectAttributes) {
        vehicleService.clearPendingImport(session);
        redirectAttributes.addFlashAttribute("success", "Import file removed.");
        return "redirect:/admin/vehicles/import";
    }

    private Map<String, Object> toVehicleRow(Vehicle vehicle) {
        Map<String, Object> row = new HashMap<>();
        row.put("id", vehicle.getId());
        row.put("plate", vehicle.getLicensePlate());
        row.put("ownerName", resolveOwnerName(vehicle));
        row.put("category", vehicle.getCategory() != null ? formatCategory(vehicle.getCategory().name()) : "-");
        row.put("makeModel", buildMakeModel(vehicle.getMake(), vehicle.getModel()));
        row.put("color", vehicle.getColor() != null && !vehicle.getColor().isBlank() ? vehicle.getColor() : "-");
        row.put("colorHex", colorToHex(vehicle.getColor()));
        row.put("status", vehicle.isActive() ? "Active" : "Blocked");
        row.put("registeredOn", vehicle.getCreatedAt() != null
                ? vehicle.getCreatedAt().toLocalDate().format(DateTimeFormatter.ISO_DATE)
                : "-");
        return row;
    }

    private String resolveOwnerName(Vehicle vehicle) {
        try {
            if (vehicle.getUser() == null) {
                return "Guest";
            }
            String firstName = vehicle.getUser().getFirstName();
            return (firstName == null || firstName.isBlank()) ? "User" : firstName;
        } catch (Exception ignored) {
            return "Guest";
        }
    }

    private Map<String, Object> buildVehicleStats(List<Vehicle> vehicles) {
        long activeCount = vehicles.stream().filter(Vehicle::isActive).count();
        long blockedCount = vehicles.size() - activeCount;

        Map<String, Object> stats = new HashMap<>();
        stats.put("total", vehicles.size());
        stats.put("parked", 0);
        stats.put("active", activeCount);
        stats.put("blocked", blockedCount);
        return stats;
    }

    private String buildMakeModel(String make, String model) {
        boolean hasMake = make != null && !make.isBlank();
        boolean hasModel = model != null && !model.isBlank();
        if (!hasMake && !hasModel) {
            return "-";
        }
        if (!hasMake) {
            return model;
        }
        if (!hasModel) {
            return make;
        }
        return make + " " + model;
    }

    private String formatCategory(String category) {
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

    private static String formatDurationMinutes(int totalMinutes) {
        if (totalMinutes <= 0) {
            return "—";
        }
        int hours = totalMinutes / 60;
        int mins = totalMinutes % 60;
        if (hours > 0 && mins > 0) {
            return hours + "h " + mins + "m";
        }
        if (hours > 0) {
            return hours + "h";
        }
        return mins + "m";
    }

    private String resolveOwnerDisplayName(Vehicle vehicle) {
        try {
            if (vehicle.getUser() == null) {
                return "Guest";
            }
            return vehicle.getUser().getFullName();
        } catch (Exception ignored) {
            return "Guest";
        }
    }
}
