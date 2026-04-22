package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.dto.VehicleRequest;
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
        model.addAttribute("vehicles", vehicleService.getAllVehicles(search, category));
        model.addAttribute("categories", vehicleService.getAllCategories());
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
        model.addAttribute("vehicle", vehicleService.getVehicleById(id));
        model.addAttribute("parkingHistory", vehicleService.getVehicleParkingHistory(id));
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
}
