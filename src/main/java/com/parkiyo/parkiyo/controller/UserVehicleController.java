package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.dto.VehicleRequest;
import com.parkiyo.parkiyo.model.User;
import com.parkiyo.parkiyo.model.Vehicle;
import com.parkiyo.parkiyo.service.UserService;
import com.parkiyo.parkiyo.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserVehicleController {

    private final VehicleService vehicleService;
    private final UserService userService;

    @GetMapping("/vehicles/add")
    @PreAuthorize("isAuthenticated()")
    public String userAddVehiclePage(Model model) {
        model.addAttribute("vehicleRequest", new VehicleRequest());
        return "vehicles/add-vehicle-user";
    }

    @GetMapping("/vehicles/quick-register")
    @PreAuthorize("isAuthenticated()")
    public String userQuickRegisterPage(Model model) {
        model.addAttribute("vehicleRequest", new VehicleRequest());
        return "vehicles/quick-register-by-plate-user";
    }

    @GetMapping({"/vehicles", "/vehicles/registry"})
    @PreAuthorize("isAuthenticated()")
    public String userVehicleRegistry(@RequestParam(required = false) String search,
                                      @RequestParam(required = false) String category,
                                      @RequestParam(required = false) String status,
                                      Authentication auth,
                                      Model model) {
        User currentUser = userService.getUserByEmail(auth.getName());
        List<Vehicle> userVehicles = vehicleService.getVehiclesByUser(auth.getName());
        boolean demoMode = userVehicles.isEmpty();
        List<Map<String, Object>> vehicleRows = demoMode
                ? demoVehicleRows(currentUser).stream()
                .filter(row -> matchesDemoSearch(row, search))
                .filter(row -> matchesDemoCategory(row, category))
                .filter(row -> matchesRegistryStatus(row, status))
                .toList()
                : userVehicles.stream()
                .filter(vehicle -> matchesSearch(vehicle, search))
                .filter(vehicle -> matchesCategory(vehicle, category))
                .map(this::toRegistryVehicleRow)
                .filter(row -> matchesRegistryStatus(row, status))
                .toList();

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("vehicles", vehicleRows);
        model.addAttribute("categories", vehicleService.getAllCategories());
        model.addAttribute("stats", demoMode ? buildRegistryVehicleStatsFromRows(vehicleRows) : buildRegistryVehicleStats(userVehicles));
        model.addAttribute("demoMode", demoMode);
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", 1);
        model.addAttribute("pageNumbers", List.of(1));
        return "vehicles/vehicle-registry-user";
    }

    @GetMapping("/vehicles/export")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> exportUserVehicles(Authentication auth) {
        List<Vehicle> vehicles = vehicleService.getVehiclesByUser(auth.getName());
        StringBuilder csv = new StringBuilder();
        csv.append("Plate,Category,Make,Model,Color,Status\n");

        for (Vehicle vehicle : vehicles) {
            csv.append(csvSafe(vehicle.getLicensePlate())).append(",")
                    .append(csvSafe(vehicle.getCategory() != null ? vehicle.getCategory().name() : "")).append(",")
                    .append(csvSafe(vehicle.getMake())).append(",")
                    .append(csvSafe(vehicle.getModel())).append(",")
                    .append(csvSafe(vehicle.getColor())).append(",")
                    .append(vehicle.isActive() ? "Active" : "Blocked")
                    .append("\n");
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=my-vehicles.csv")
                .contentType(new MediaType("text", "csv"))
                .body(csv.toString());
    }

    @PostMapping("/vehicles/create")
    @PreAuthorize("isAuthenticated()")
    public String createUserVehicle(@Valid @ModelAttribute VehicleRequest request,
                                    BindingResult bindingResult,
                                    Model model,
                                    Authentication auth,
                                    RedirectAttributes redirectAttributes) {
        validateEntryVehicleRegistration(request, bindingResult);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Please complete the required vehicle details.");
            redirectAttributes.addFlashAttribute("showVehicleRegistration", true);
            redirectAttributes.addFlashAttribute("vehicleRequest", request);
            return "redirect:/entry";
        }
        
        try {
            User currentUser = userService.getUserByEmail(auth.getName());
            request.setUserId(currentUser.getId());
            vehicleService.createVehicle(request);
            redirectAttributes.addFlashAttribute("success", "Vehicle registered successfully.");
            redirectAttributes.addFlashAttribute("registeredPlate", request.getLicensePlate());
            return "redirect:/entry";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("showVehicleRegistration", true);
            redirectAttributes.addFlashAttribute("vehicleRequest", request);
            return "redirect:/entry";
        }
    }

    @PostMapping("/vehicles/quick-register")
    @PreAuthorize("isAuthenticated()")
    public String createUserQuickVehicle(@Valid @ModelAttribute VehicleRequest request,
                                         BindingResult bindingResult,
                                         Model model,
                                         Authentication auth,
                                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("vehicleRequest", request);
            return "vehicles/quick-register-by-plate-user";
        }
        
        try {
            User currentUser = userService.getUserByEmail(auth.getName());
            request.setUserId(currentUser.getId());
            vehicleService.createVehicle(request);
            redirectAttributes.addFlashAttribute("success", "Vehicle registered successfully.");
            return "redirect:/vehicles/quick-register";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/vehicles/quick-register";
        }
    }

    private Map<String, Object> toRegistryVehicleRow(Vehicle vehicle) {
        Map<String, Object> row = new HashMap<>();
        row.put("id", vehicle.getId());
        row.put("demo", false);
        row.put("plate", vehicle.getLicensePlate());
        row.put("ownerName", vehicle.getUser() != null && vehicle.getUser().getFullName() != null
                ? vehicle.getUser().getFullName()
                : "You");
        row.put("category", vehicle.getCategory() != null ? formatCategory(vehicle.getCategory().name()) : "-");
        row.put("make", valueOrDash(vehicle.getMake()));
        row.put("model", valueOrDash(vehicle.getModel()));
        row.put("makeModel", buildMakeModel(vehicle.getMake(), vehicle.getModel()));
        row.put("year", vehicle.getYear() != null ? vehicle.getYear() : "-");
        row.put("color", vehicle.getColor() != null && !vehicle.getColor().isBlank() ? vehicle.getColor() : "-");
        row.put("colorHex", colorToHex(vehicle.getColor()));
        row.put("status", vehicle.isActive() ? "Active" : "Inactive");
        row.put("parkingStatus", vehicleService.isVehicleCurrentlyParked(vehicle.getId()) ? "Parked" : "Not Parked");
        row.put("registeredOn", vehicle.getCreatedAt() != null
                ? vehicle.getCreatedAt().toLocalDate().format(DateTimeFormatter.ISO_DATE)
                : "-");
        return row;
    }

    private List<Map<String, Object>> demoVehicleRows(User currentUser) {
        String ownerName = currentUser != null && currentUser.getFullName() != null && !currentUser.getFullName().isBlank()
                ? currentUser.getFullName()
                : "Current User";
        return List.of(
                demoVehicleRow("CAB-4589", "Car", "Toyota", "Prius", "2022", "White", "Active", ownerName),
                demoVehicleRow("BIK-2241", "Motorcycle", "Yamaha", "FZ", "2021", "Blue", "Active", ownerName),
                demoVehicleRow("VAN-7782", "Van", "Nissan", "Caravan", "2020", "Silver", "Inactive", ownerName),
                demoVehicleRow("TRK-9021", "Truck", "Isuzu", "Elf", "2019", "Black", "Active", ownerName)
        );
    }

    private Map<String, Object> demoVehicleRow(String plate,
                                               String category,
                                               String make,
                                               String model,
                                               String year,
                                               String color,
                                               String status,
                                               String ownerName) {
        Map<String, Object> row = new HashMap<>();
        row.put("id", "demo-" + plate);
        row.put("demo", true);
        row.put("plate", plate);
        row.put("ownerName", ownerName);
        row.put("category", category);
        row.put("make", make);
        row.put("model", model);
        row.put("makeModel", make + " " + model);
        row.put("year", year);
        row.put("color", color);
        row.put("colorHex", colorToHex(color));
        row.put("status", status);
        row.put("parkingStatus", "Not Parked");
        row.put("registeredOn", "Demo");
        return row;
    }

    private void validateEntryVehicleRegistration(VehicleRequest request, BindingResult bindingResult) {
        if (request.getMake() == null || request.getMake().isBlank()) {
            bindingResult.rejectValue("make", "vehicle.make.required", "Make is required.");
        }
        if (request.getModel() == null || request.getModel().isBlank()) {
            bindingResult.rejectValue("model", "vehicle.model.required", "Model is required.");
        }
        if (request.getOwnerFirstName() == null || request.getOwnerFirstName().isBlank()) {
            bindingResult.rejectValue("ownerFirstName", "vehicle.owner.required", "Owner name is required.");
        }
    }

    private Map<String, Object> buildRegistryVehicleStats(List<Vehicle> vehicles) {
        long activeCount = vehicles.stream().filter(Vehicle::isActive).count();
        long inactiveCount = vehicles.size() - activeCount;
        long parkedCount = vehicles.stream()
                .filter(vehicle -> vehicle.getId() != null && vehicleService.isVehicleCurrentlyParked(vehicle.getId()))
                .count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("total", vehicles.size());
        stats.put("parked", parkedCount);
        stats.put("active", activeCount);
        stats.put("inactive", inactiveCount);
        return stats;
    }

    private Map<String, Object> buildRegistryVehicleStatsFromRows(List<Map<String, Object>> rows) {
        long activeCount = rows.stream()
                .filter(row -> "Active".equalsIgnoreCase(String.valueOf(row.get("status"))))
                .count();
        long parkedCount = rows.stream()
                .filter(row -> "Parked".equalsIgnoreCase(String.valueOf(row.get("parkingStatus"))))
                .count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("total", rows.size());
        stats.put("parked", parkedCount);
        stats.put("active", activeCount);
        stats.put("inactive", rows.size() - activeCount);
        return stats;
    }

    private String csvSafe(String value) {
        if (value == null) {
            return "";
        }
        String escaped = value.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }

    private boolean matchesSearch(Vehicle vehicle, String search) {
        if (search == null || search.isBlank()) {
            return true;
        }
        String q = search.toLowerCase(Locale.ROOT);
        return (vehicle.getLicensePlate() != null && vehicle.getLicensePlate().toLowerCase(Locale.ROOT).contains(q))
                || (vehicle.getMake() != null && vehicle.getMake().toLowerCase(Locale.ROOT).contains(q))
                || (vehicle.getModel() != null && vehicle.getModel().toLowerCase(Locale.ROOT).contains(q));
    }

    private boolean matchesCategory(Vehicle vehicle, String category) {
        if (category == null || category.isBlank()) {
            return true;
        }
        return vehicle.getCategory() != null && vehicle.getCategory().name().equalsIgnoreCase(category);
    }

    private boolean matchesDemoSearch(Map<String, Object> row, String search) {
        if (search == null || search.isBlank()) {
            return true;
        }
        String q = search.toLowerCase(Locale.ROOT);
        return String.valueOf(row.getOrDefault("plate", "")).toLowerCase(Locale.ROOT).contains(q)
                || String.valueOf(row.getOrDefault("make", "")).toLowerCase(Locale.ROOT).contains(q)
                || String.valueOf(row.getOrDefault("model", "")).toLowerCase(Locale.ROOT).contains(q);
    }

    private boolean matchesDemoCategory(Map<String, Object> row, String category) {
        if (category == null || category.isBlank()) {
            return true;
        }
        String displayCategory = String.valueOf(row.getOrDefault("category", ""));
        return displayCategory.equalsIgnoreCase(category) || displayCategory.replace(" ", "_").equalsIgnoreCase(category);
    }

    private boolean matchesRegistryStatus(Map<String, Object> row, String status) {
        if (status == null || status.isBlank()) {
            return true;
        }
        String requested = status.trim().toLowerCase(Locale.ROOT);
        String activeStatus = String.valueOf(row.getOrDefault("status", "")).toLowerCase(Locale.ROOT);
        String parkingStatus = String.valueOf(row.getOrDefault("parkingStatus", "")).toLowerCase(Locale.ROOT);
        return activeStatus.equals(requested) || parkingStatus.equals(requested);
    }

    private String buildMakeModel(String make, String model) {
        boolean hasMake = make != null && !make.isBlank();
        boolean hasModel = model != null && !model.isBlank();
        if (!hasMake && !hasModel) return "-";
        if (!hasMake) return model;
        if (!hasModel) return make;
        return make + " " + model;
    }

    private String valueOrDash(String value) {
        return value != null && !value.isBlank() ? value : "-";
    }

    private String formatCategory(String category) {
        if (category == null || category.isBlank()) return "-";
        return category.substring(0, 1).toUpperCase(Locale.ROOT) + category.substring(1).toLowerCase(Locale.ROOT);
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
}
