package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.dto.SlotRequest;
import com.parkiyo.parkiyo.dto.BatchSlotRequest;
import com.parkiyo.parkiyo.service.SlotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/slots")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class SlotController {

    private final SlotService slotService;

    // GET /admin/slots  - slot list
    @GetMapping
    public String slotList(@RequestParam(required = false) String status,
                           @RequestParam(required = false) String zone,
                           Model model) {
        model.addAttribute("slots", slotService.getSlots(status, zone));
        model.addAttribute("zones", slotService.getAllZones());
        return "slot-list";
    }

    // GET /admin/slots/overview
    @GetMapping("/overview")
    public String slotOverview(Model model) {
        model.addAttribute("overview", slotService.getSlotOverview());
        model.addAttribute("zones", slotService.getZoneSummaries());
        return "slot-overview";
    }

    // GET /admin/slots/add
    @GetMapping("/add")
    public String addSlotPage(Model model) {
        model.addAttribute("slotRequest", new SlotRequest());
        model.addAttribute("zones", slotService.getAllZones());
        return "add-slot";
    }

    // POST /admin/slots/create
    @PostMapping("/create")
    public String createSlot(@Valid @ModelAttribute SlotRequest request,
                             RedirectAttributes redirectAttributes) {
        try {
            slotService.createSlot(request);
            redirectAttributes.addFlashAttribute("success", "Slot created successfully.");
            return "redirect:/admin/slots";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/slots/add";
        }
    }

    // GET /admin/slots/{id}/edit
    @GetMapping("/{id}/edit")
    public String editSlotPage(@PathVariable Long id, Model model) {
        model.addAttribute("slot", slotService.getSlotById(id));
        model.addAttribute("zones", slotService.getAllZones());
        return "edit-slot";
    }

    // POST /admin/slots/update
    @PostMapping("/update")
    public String updateSlot(@RequestParam Long id,
                             @Valid @ModelAttribute SlotRequest request,
                             RedirectAttributes redirectAttributes) {
        try {
            slotService.updateSlot(id, request);
            redirectAttributes.addFlashAttribute("success", "Slot updated.");
            return "redirect:/admin/slots";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/slots/" + id + "/edit";
        }
    }

    // POST /admin/slots/delete
    @PostMapping("/delete")
    public String deleteSlot(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            slotService.deleteSlot(id);
            redirectAttributes.addFlashAttribute("success", "Slot deleted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/slots";
    }

    // GET /admin/slots/batch
    @GetMapping("/batch")
    public String batchSlotPage(Model model) {
        model.addAttribute("batchRequest", new BatchSlotRequest());
        model.addAttribute("zones", slotService.getAllZones());
        return "batchslot-generate";
    }

    // POST /admin/slots/batch
    @PostMapping("/batch")
    public String batchGenerateSlots(@Valid @ModelAttribute BatchSlotRequest request,
                                     RedirectAttributes redirectAttributes) {
        try {
            int count = slotService.batchGenerateSlots(request);
            redirectAttributes.addFlashAttribute("success", count + " slots generated.");
            return "redirect:/admin/slots";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/slots/batch";
        }
    }

    // GET /admin/slots/{id}/usage-history
    @GetMapping("/{id}/usage-history")
    public String slotUsageHistory(@PathVariable Long id, Model model) {
        model.addAttribute("slot", slotService.getSlotById(id));
        model.addAttribute("history", slotService.getSlotUsageHistory(id));
        return "slot-usage-history";
    }
}
