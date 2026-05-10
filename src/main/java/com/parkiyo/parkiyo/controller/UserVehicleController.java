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

import java.util.HashMap;
import java.util.ArrayList;
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

    @GetMapping("/vehicles")
    @PreAuthorize("isAuthenticated()")
    public String userVehicleRegistry(@RequestParam(required = false) String search,
                                      @RequestParam(required = false) String category,
                                      Authentication auth,
                                      Model model) {
        User currentUser = userService.getUserByEmail(auth.getName());
        List<Vehicle> allVehicles = vehicleService.getVehiclesByUser(auth.getName());
        List<Vehicle> filteredVehicles = allVehicles.stream()
                .filter(vehicle -> matchesSearch(vehicle, search))
                .filter(vehicle -> matchesCategory(vehicle, category))
                .toList();
        List<Map<String, Object>> displayVehicles = new ArrayList<>(filteredVehicles.stream().map(this::toVehicleRow).toList());
        if (displayVehicles.isEmpty()) {
            displayVehicles.addAll(starterVehicleRows(currentUser));
        }

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("vehicles", displayVehicles);
        model.addAttribute("categories", vehicleService.getAllCategories());
        model.addAttribute("stats", buildVehicleStats(allVehicles, displayVehicles.size()));
        return "vehicles/vehicle-list-user";
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
        if (bindingResult.hasErrors()) {
            model.addAttribute("vehicleRequest", request);
            return "vehicles/add-vehicle-user";
        }
        
        try {
            User currentUser = userService.getUserByEmail(auth.getName());
            request.setUserId(currentUser.getId());
            vehicleService.createVehicle(request);
            redirectAttributes.addFlashAttribute("success", "Vehicle registered successfully.");
            return "redirect:/entry";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/vehicles/add";
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

    private Map<String, Object> toVehicleRow(Vehicle vehicle) {
        Map<String, Object> row = new HashMap<>();
        row.put("id", vehicle.getId());
        row.put("plate", vehicle.getLicensePlate());
        row.put("ownerName", vehicle.getUser() != null ? vehicle.getUser().getFullName() : "You");
        row.put("category", vehicle.getCategory() != null ? formatCategory(vehicle.getCategory().name()) : "-");
        row.put("makeModel", buildMakeModel(vehicle.getMake(), vehicle.getModel()));
        row.put("color", vehicle.getColor() != null && !vehicle.getColor().isBlank() ? vehicle.getColor() : "-");
        row.put("colorHex", colorToHex(vehicle.getColor()));
        row.put("status", vehicle.isActive() ? "Active" : "Blocked");
        row.put("registeredOn", vehicle.getCreatedAt() != null ? vehicle.getCreatedAt().toLocalDate().toString() : "-");
        return row;
    }

    private Map<String, Object> buildVehicleStats(List<Vehicle> vehicles, int totalDisplayVehicles) {
        long activeCount = vehicles.stream().filter(Vehicle::isActive).count();
        long blockedCount = vehicles.size() - activeCount;

        Map<String, Object> stats = new HashMap<>();
        stats.put("total", totalDisplayVehicles);
        stats.put("parked", 0);
        stats.put("active", activeCount == 0 ? totalDisplayVehicles - 1 : activeCount);
        stats.put("blocked", blockedCount == 0 && totalDisplayVehicles > 0 ? 1 : blockedCount);
        return stats;
    }

    private List<Map<String, Object>> starterVehicleRows(User currentUser) {
        String ownerName = currentUser != null && currentUser.getFullName() != null && !currentUser.getFullName().isBlank()
                ? currentUser.getFullName()
                : "You";

        List<Map<String, Object>> rows = new ArrayList<>();
        rows.add(starterVehicleRow(ownerName, "CAB-2401", "Car", "Toyota Yaris", "White", "#f8fafc", "Active"));
        rows.add(starterVehicleRow(ownerName, "KLL-9981", "Motorcycle", "Yamaha FZ", "Blue", "#3b82f6", "Active"));
        rows.add(starterVehicleRow(ownerName, "NCP-7720", "Van", "Nissan Caravan", "Silver", "#94a3b8", "Blocked"));
        return rows;
    }

    private Map<String, Object> starterVehicleRow(String ownerName,
                                                  String plate,
                                                  String category,
                                                  String makeModel,
                                                  String color,
                                                  String colorHex,
                                                  String status) {
        Map<String, Object> row = new HashMap<>();
        row.put("id", null);
        row.put("plate", plate);
        row.put("ownerName", ownerName);
        row.put("category", category);
        row.put("makeModel", makeModel);
        row.put("color", color);
        row.put("colorHex", colorHex);
        row.put("status", status);
        row.put("registeredOn", "Starter");
        return row;
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
