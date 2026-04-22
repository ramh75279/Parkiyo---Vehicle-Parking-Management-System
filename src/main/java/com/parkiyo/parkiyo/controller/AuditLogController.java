package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/audit")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    // GET /admin/audit
    @GetMapping
    public String auditLog(@RequestParam(required = false) String action,
                           @RequestParam(required = false) String user,
                           @RequestParam(required = false) String dateFrom,
                           @RequestParam(required = false) String dateTo,
                           Model model) {
        model.addAttribute("logs", auditLogService.getLogs(action, user, dateFrom, dateTo));
        model.addAttribute("actions", auditLogService.getAllActionTypes());
        return "admin/auditlog";
    }

    // GET /admin/audit/{id}
    @GetMapping("/{id}")
    public String auditLogDetails(@PathVariable Long id, Model model) {
        model.addAttribute("log", auditLogService.getLogById(id));
        return "admin/auditlogdeatils";
    }
}
