package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.enums.PassPlanCategory;
import com.parkiyo.parkiyo.model.PassPlan;
import com.parkiyo.parkiyo.service.PassPlanService;
import com.parkiyo.parkiyo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/admin/pass-plans")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminPassPlanController {

    private final PassPlanService passPlanService;
    private final UserService userService;

    @GetMapping
    public String list(Model model, org.springframework.security.core.Authentication auth) {
        model.addAttribute("plans", passPlanService.listAllForAdmin());
        model.addAttribute("currentUser", userService.getUserByEmail(auth.getName()));
        return "passes/admin/pass-plan-list";
    }

    @GetMapping("/new")
    public String createForm(Model model, org.springframework.security.core.Authentication auth) {
        model.addAttribute("plan", PassPlan.builder()
                .category(PassPlanCategory.MONTHLY)
                .durationDays(30)
                .price(BigDecimal.ZERO)
                .discountPercent(BigDecimal.ZERO)
                .active(true)
                .displayOrder(0)
                .build());
        model.addAttribute("categories", PassPlanCategory.values());
        model.addAttribute("currentUser", userService.getUserByEmail(auth.getName()));
        model.addAttribute("isEdit", false);
        return "passes/admin/pass-plan-form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, org.springframework.security.core.Authentication auth) {
        model.addAttribute("plan", passPlanService.getById(id));
        model.addAttribute("categories", PassPlanCategory.values());
        model.addAttribute("currentUser", userService.getUserByEmail(auth.getName()));
        model.addAttribute("isEdit", true);
        return "passes/admin/pass-plan-form";
    }

    @PostMapping("/save")
    public String save(@RequestParam(required = false) Long id,
                       @RequestParam String name,
                       @RequestParam(required = false) String description,
                       @RequestParam PassPlanCategory category,
                       @RequestParam BigDecimal price,
                       @RequestParam int durationDays,
                       @RequestParam(required = false) String zone,
                       @RequestParam(defaultValue = "false") boolean unlimitedParking,
                       @RequestParam BigDecimal discountPercent,
                       @RequestParam String active,
                       @RequestParam(defaultValue = "0") int displayOrder,
                       RedirectAttributes redirectAttributes) {
        try {
            boolean catalogActive = Boolean.parseBoolean(active);
            passPlanService.savePlan(id, name, description, category, price, durationDays, zone,
                    unlimitedParking, discountPercent, catalogActive, displayOrder);
            redirectAttributes.addFlashAttribute("success", "Pass plan saved.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            if (id != null) {
                return "redirect:/admin/pass-plans/" + id + "/edit";
            }
            return "redirect:/admin/pass-plans/new";
        }
        return "redirect:/admin/pass-plans";
    }

    @PostMapping("/{id}/deactivate")
    public String deactivate(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            passPlanService.deactivate(id);
            redirectAttributes.addFlashAttribute("success", "Plan deactivated (hidden from users).");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/pass-plans";
    }
}
