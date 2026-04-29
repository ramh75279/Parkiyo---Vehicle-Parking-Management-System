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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        return "slots/slot-list";
    }

    // GET /admin/slots/overview
    @GetMapping("/overview")
    public String slotOverview(Model model) {
        model.addAttribute("overview", slotService.getSlotOverview());
        model.addAttribute("zones", slotService.getZoneSummaries());
        return "slots/slot-overview";
    }

    // GET /admin/slots/add
    @GetMapping("/add")
    public String addSlotPage(Model model) {
        model.addAttribute("slotRequest", new SlotRequest());
        model.addAttribute("zones", slotService.getAllZones());
        return "slots/add-slot";
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
    public String editSlotPage(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("slot", slotService.getSlotById(id));
        } catch (RuntimeException ex) {
            List<String> slotNumbers = resolveOverviewSlotNumbers(id);
            for (String slotNumber : slotNumbers) {
                var resolved = slotService.findSlotByNumber(slotNumber);
                if (resolved.isPresent()) {
                    model.addAttribute("slot", resolved.get());
                    model.addAttribute("zones", slotService.getAllZones());
                    return "slots/edit-slot";
                }
            }
            String label = slotNumbers.isEmpty() ? String.valueOf(id) : slotNumbers.get(0);
            redirectAttributes.addFlashAttribute("error", "Slot " + label + " is not configured in database.");
            return "redirect:/admin/slots/overview";
        }
        model.addAttribute("zones", slotService.getAllZones());
        return "slots/edit-slot";
    }

    @GetMapping("/number/{slotNumber}/edit")
    public String editSlotByNumber(@PathVariable String slotNumber, Model model, RedirectAttributes redirectAttributes) {
        for (String candidate : resolveSlotNumberCandidates(slotNumber)) {
            var resolved = slotService.findSlotByNumber(candidate);
            if (resolved.isPresent()) {
                model.addAttribute("slot", resolved.get());
                model.addAttribute("zones", slotService.getAllZones());
                return "slots/edit-slot";
            }
        }
        List<String> candidates = resolveSlotNumberCandidates(slotNumber);
        String newSlotNumber = candidates.isEmpty() ? slotNumber : candidates.get(0);
        model.addAttribute("slot", slotService.getOrCreateSlotByNumber(newSlotNumber));
        model.addAttribute("zones", slotService.getAllZones());
        return "slots/edit-slot";
    }

    private List<String> resolveOverviewSlotNumbers(Long overviewId) {
        List<String> candidates = new ArrayList<>();
        if (overviewId == null) {
            return candidates;
        }
        long id = overviewId;
        if (id >= 1 && id <= 15) {
            candidates.add("A-" + String.format("%02d", id));
            candidates.add("A-" + String.format("%03d", id));
            return candidates;
        }
        if (id >= 21 && id <= 31) {
            candidates.add("B-" + String.format("%02d", id - 20));
            candidates.add("B-" + String.format("%03d", id - 20));
            return candidates;
        }
        if (id >= 51 && id <= 60) {
            candidates.add("C-" + String.format("%02d", id - 50));
            candidates.add("C-" + String.format("%03d", id - 50));
            return candidates;
        }
        if (id >= 76 && id <= 85) {
            candidates.add("D-" + String.format("%02d", id - 75));
            candidates.add("D-" + String.format("%03d", id - 75));
            return candidates;
        }
        return candidates;
    }

    private List<String> resolveSlotNumberCandidates(String slotNumber) {
        List<String> candidates = new ArrayList<>();
        if (slotNumber == null || slotNumber.isBlank()) {
            return candidates;
        }

        String normalized = slotNumber.trim().toUpperCase(Locale.ROOT);
        candidates.add(normalized);

        String[] parts = normalized.split("-");
        if (parts.length == 2) {
            String prefix = parts[0];
            String numeric = parts[1];
            if (numeric.matches("\\d+")) {
                int n = Integer.parseInt(numeric);
                String twoDigit = prefix + "-" + String.format("%02d", n);
                String threeDigit = prefix + "-" + String.format("%03d", n);
                if (!candidates.contains(twoDigit)) {
                    candidates.add(twoDigit);
                }
                if (!candidates.contains(threeDigit)) {
                    candidates.add(threeDigit);
                }
            }
        }
        return candidates;
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
        return "slots/batchslot-generate";
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
        return "slots/slot-usage-history";
    }
}
