package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.dto.EntryRequest;
import com.parkiyo.parkiyo.dto.VehicleRequest;
import com.parkiyo.parkiyo.enums.VehicleCategory;
import com.parkiyo.parkiyo.model.Vehicle;
import com.parkiyo.parkiyo.service.EntryService;
import com.parkiyo.parkiyo.service.SlotService;
import com.parkiyo.parkiyo.service.UserService;
import com.parkiyo.parkiyo.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class EntryController {

    private final EntryService entryService;
    private final SlotService slotService;
    private final VehicleService vehicleService;
    private final UserService userService;

    // ─── USER ────────────────────────────────────────────────────────────────

    // GET /entry
    @GetMapping("/entry")
    @PreAuthorize("isAuthenticated()")
    public String userEntryPage(Authentication auth, Model model) {
        var slotOverview = slotService.getSlotOverview();
        model.addAttribute("availableSlots", slotService.getAvailableSlots());
        model.addAttribute("occupiedSlots", slotOverview.get("occupied"));
        model.addAttribute("totalSlots", slotOverview.get("total"));
        model.addAttribute("occupancyRate", slotOverview.get("occupancyRate"));
        model.addAttribute("userVehicles", vehicleService.getVehiclesByUser(auth.getName()));
        model.addAttribute("entryRequest", new EntryRequest());
        if (!model.containsAttribute("vehicleRequest")) {
            model.addAttribute("vehicleRequest", new VehicleRequest());
        }
        return "parking/entry";
    }

    @GetMapping(value = "/entry/vehicle-lookup", produces = "application/json")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public Map<String, Object> lookupVehicle(@RequestParam String licensePlate) {
        return lookupVehicleByPlate(licensePlate);
    }

    private Map<String, Object> lookupVehicleByPlate(String licensePlate) {
        Map<String, Object> response = new HashMap<>();
        vehicleService.findVehicleByLicensePlate(licensePlate).ifPresentOrElse(vehicle -> {
            response.put("found", true);
            response.put("vehicle", buildVehiclePayload(vehicle));
        }, () -> response.put("found", false));
        return response;
    }

    // POST /entry
    @PostMapping("/entry")
    @PreAuthorize("isAuthenticated()")
    public String processUserEntry(@Valid @ModelAttribute EntryRequest request,
                                   Authentication auth,
                                   RedirectAttributes redirectAttributes) {
        try {
            entryService.processEntry(request, auth.getName());
            redirectAttributes.addFlashAttribute("success", "Vehicle entry recorded successfully.");
            return "redirect:/parking";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/entry";
        }
    }

    private Map<String, Object> buildVehiclePayload(Vehicle vehicle) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("licensePlate", vehicle.getLicensePlate());
        payload.put("category", vehicle.getCategory() != null ? vehicle.getCategory().name() : "");
        payload.put("make", vehicle.getMake() != null ? vehicle.getMake() : "");
        payload.put("model", vehicle.getModel() != null ? vehicle.getModel() : "");
        payload.put("color", vehicle.getColor() != null ? vehicle.getColor() : "");
        payload.put("year", vehicle.getYear() != null ? vehicle.getYear() : "");
        payload.put("ownerName", vehicle.getUser() != null && vehicle.getUser().getFullName() != null ? vehicle.getUser().getFullName() : "User");
        return payload;
    }

    // ─── ADMIN ───────────────────────────────────────────────────────────────

    // GET /admin/entry
    @GetMapping("/admin/entry")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminEntryPage(Authentication auth, Model model) {
        if (auth != null) {
            model.addAttribute("currentUser", userService.getUserByEmail(auth.getName()));
        }
        model.addAttribute("availableSlots", slotService.getAvailableSlots());
        model.addAttribute("slotOverview", slotService.getSlotOverview());
        model.addAttribute("recentEntries", entryService.getRecentEntries(20));
        model.addAttribute("entryRequest", new EntryRequest());
        model.addAttribute("categories", VehicleCategory.values());
        if (!model.containsAttribute("vehicleRequest")) {
            model.addAttribute("vehicleRequest", new VehicleRequest());
        }
        return "parking/entry-admin";
    }

    @GetMapping(value = "/admin/entry/vehicle-lookup", produces = "application/json")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> lookupVehicleForAdmin(@RequestParam String licensePlate) {
        return lookupVehicleByPlate(licensePlate);
    }

    @PostMapping("/admin/entry/register-vehicle")
    @PreAuthorize("hasRole('ADMIN')")
    public String registerVehicleFromAdminEntry(@Valid @ModelAttribute VehicleRequest request,
                                                BindingResult bindingResult,
                                                RedirectAttributes redirectAttributes) {
        validateEntryVehicleRegistration(request, bindingResult);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Please complete the required vehicle details.");
            redirectAttributes.addFlashAttribute("showVehicleRegistration", true);
            redirectAttributes.addFlashAttribute("vehicleRequest", request);
            return "redirect:/admin/entry";
        }

        try {
            vehicleService.createVehicle(request);
            redirectAttributes.addFlashAttribute("success", "Vehicle registered successfully.");
            redirectAttributes.addFlashAttribute("registeredPlate", request.getLicensePlate());
            return "redirect:/admin/entry";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("showVehicleRegistration", true);
            redirectAttributes.addFlashAttribute("vehicleRequest", request);
            return "redirect:/admin/entry";
        }
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

    // POST /admin/entry
    @PostMapping("/admin/entry")
    @PreAuthorize("hasRole('ADMIN')")
    public String processAdminEntry(@Valid @ModelAttribute EntryRequest request,
                                    Authentication auth,
                                    RedirectAttributes redirectAttributes) {
        try {
            entryService.processEntry(request, auth.getName());
            redirectAttributes.addFlashAttribute("success", "Vehicle entry logged.");
            return "redirect:/admin/entry";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/entry";
        }
    }
}
