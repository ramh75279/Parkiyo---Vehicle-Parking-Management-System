package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.dto.VehicleRequest;
import com.parkiyo.parkiyo.model.User;
import com.parkiyo.parkiyo.service.UserService;
import com.parkiyo.parkiyo.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @PostMapping("/vehicles/create")
    @PreAuthorize("isAuthenticated()")
    public String createUserVehicle(@Valid @ModelAttribute VehicleRequest request,
                                    Authentication auth,
                                    RedirectAttributes redirectAttributes) {
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

    @GetMapping("/vehicles/quick-register")
    @PreAuthorize("isAuthenticated()")
    public String userQuickRegisterPage(@RequestParam(required = false) String plate, Model model) {
        model.addAttribute("plate", plate != null ? plate.toUpperCase() : "");
        return "vehicles/quick-register-by-plate";
    }

    @PostMapping("/vehicles/quick-register")
    @PreAuthorize("isAuthenticated()")
    public String userQuickRegister(@RequestParam String licensePlate,
                                    @RequestParam(required = false) String category,
                                    Authentication auth,
                                    RedirectAttributes redirectAttributes) {
        try {
            vehicleService.quickRegisterByPlate(licensePlate, category);
            redirectAttributes.addFlashAttribute("success", "Vehicle " + licensePlate.toUpperCase() + " registered.");
            return "redirect:/entry";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/vehicles/quick-register";
        }
    }
}