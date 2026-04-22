package com.parkiyo.parkiyo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@PreAuthorize("isAuthenticated()")
public class CompatibilityController {

    @GetMapping("/admin/audit-log")
    @PreAuthorize("hasRole('ADMIN')")
    public String auditLogAlias() {
        return "redirect:/admin/audit";
    }

    @GetMapping("/admin/audit-log/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String auditLogDetailsAlias(@PathVariable Long id) {
        return "redirect:/admin/audit/" + id;
    }

    @GetMapping({"/admin/account-settings", "/account/settings/2fa/disable", "/account/settings/sessions/revoke"})
    public String accountSettingsAlias() {
        return "redirect:/account/settings";
    }

    @GetMapping("/admin/slots/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String slotEditAlias() {
        return "redirect:/admin/slots";
    }

    @GetMapping("/admin/vehicles/by-category")
    @PreAuthorize("hasRole('ADMIN')")
    public String vehicleCategoryAlias() {
        return "redirect:/admin/vehicles/browse";
    }

    @GetMapping({"/admin/vehicles/details", "/admin/vehicles/history"})
    @PreAuthorize("hasRole('ADMIN')")
    public String vehicleListAlias() {
        return "redirect:/admin/vehicles";
    }

    @GetMapping("/admin/vehicles/{id}/history")
    @PreAuthorize("hasRole('ADMIN')")
    public String vehicleHistoryAlias(@PathVariable Long id) {
        return "redirect:/admin/vehicles/" + id;
    }

    @GetMapping("/admin/exit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminExitAlias() {
        return "redirect:/admin/exit";
    }
}
