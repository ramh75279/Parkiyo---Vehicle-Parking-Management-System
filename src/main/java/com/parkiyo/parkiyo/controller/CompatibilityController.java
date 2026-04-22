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

    @GetMapping("/admin/slots/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String slotEditByIdAlias(@PathVariable Long id) {
        return "redirect:/admin/slots/" + id + "/edit";
    }

    @GetMapping({"/admin/slots/history", "/admin/slots/history/export"})
    @PreAuthorize("hasRole('ADMIN')")
    public String slotHistoryAlias() {
        return "redirect:/admin/slots";
    }

    @GetMapping("/admin/slots/history/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String slotHistoryByIdAlias(@PathVariable Long id) {
        return "redirect:/admin/slots/" + id + "/usage-history";
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

    @GetMapping({"/admin/payments/export", "/admin/receipts"})
    @PreAuthorize("hasRole('ADMIN')")
    public String adminPaymentExportAlias() {
        return "redirect:/admin/payments";
    }

    @GetMapping("/admin/receipts/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminReceiptAlias(@PathVariable Long id) {
        return "redirect:/admin/payments";
    }

    @GetMapping({"/admin/reports/audit-log", "/admin/reports/daily/export"})
    @PreAuthorize("hasRole('ADMIN')")
    public String reportShortcutAlias() {
        return "redirect:/admin/reports";
    }

    @GetMapping("/admin/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String userDetailsAlias(@PathVariable Long id) {
        return "redirect:/admin/users/" + id + "/edit";
    }

    @GetMapping({"/payments", "/payments/pending", "/payments/processing"})
    public String paymentShortcutAlias() {
        return "redirect:/payments/history";
    }

    @GetMapping({"/parking/record", "/parking/record-details"})
    public String parkingShortcutAlias() {
        return "redirect:/parking";
    }

    @GetMapping({"/slots", "/slots/select"})
    public String slotSelectionAlias() {
        return "redirect:/reservations/slot-selection";
    }

    @GetMapping("/vehicle/profile")
    public String vehicleProfileAlias() {
        return "redirect:/parking";
    }
}
