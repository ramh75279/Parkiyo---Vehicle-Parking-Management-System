package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.dto.SavedReportRequest;
import com.parkiyo.parkiyo.enums.SavedReportPeriod;
import com.parkiyo.parkiyo.enums.SavedReportStatus;
import com.parkiyo.parkiyo.enums.SavedReportType;
import com.parkiyo.parkiyo.service.AuditLogService;
import com.parkiyo.parkiyo.service.ReportService;
import com.parkiyo.parkiyo.service.SavedReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Map;

@Controller
@RequestMapping("/admin/reports")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final SavedReportService savedReportService;
    private final AuditLogService auditLogService;

    @GetMapping
    public String reportsHub(
            @RequestParam(required = false) SavedReportType type,
            @RequestParam(required = false) SavedReportStatus status,
            @RequestParam(required = false) String q,
            Model model) {
        enrichSummary(model);
        model.addAttribute("savedReports", savedReportService.findFiltered(type, status, q));
        model.addAttribute("filterType", type);
        model.addAttribute("filterStatus", status);
        model.addAttribute("searchQuery", q != null ? q : "");
        model.addAttribute("reportTypes", SavedReportType.values());
        model.addAttribute("reportPeriods", SavedReportPeriod.values());
        model.addAttribute("reportStatuses", SavedReportStatus.values());
        SavedReportRequest emptyForm = new SavedReportRequest();
        emptyForm.setReportType(SavedReportType.REVENUE);
        emptyForm.setPeriod(SavedReportPeriod.DAILY);
        emptyForm.setStatus(SavedReportStatus.DRAFT);
        model.addAttribute("savedReportRequest", emptyForm);
        return "reports/repportshubpage";
    }

    @PostMapping
    public String createSavedReport(
            @Valid @ModelAttribute("savedReportRequest") SavedReportRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            enrichSummary(model);
            model.addAttribute("savedReports",
                    savedReportService.findFiltered(null, null, null));
            model.addAttribute("filterType", null);
            model.addAttribute("filterStatus", null);
            model.addAttribute("searchQuery", "");
            model.addAttribute("reportTypes", SavedReportType.values());
            model.addAttribute("reportPeriods", SavedReportPeriod.values());
            model.addAttribute("reportStatuses", SavedReportStatus.values());
            model.addAttribute("error",
                    bindingResult.getFieldError() != null
                            ? bindingResult.getFieldError().getDefaultMessage()
                            : "Please fix the form errors.");
            return "reports/repportshubpage";
        }
        var saved = savedReportService.create(request);
        auditLogService.logAction(
                "SAVED_REPORT_CREATED",
                "SavedReport",
                saved.getId(),
                "Created saved report: " + saved.getTitle());
        redirectAttributes.addFlashAttribute("success", "Report \"" + saved.getTitle() + "\" created.");
        return "redirect:/admin/reports";
    }

    @PostMapping("/{id}/update")
    public String updateSavedReport(
            @PathVariable Long id,
            @Valid @ModelAttribute("savedReportRequest") SavedReportRequest request,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error",
                    bindingResult.getFieldError() != null
                            ? bindingResult.getFieldError().getDefaultMessage()
                            : "Validation failed.");
            return "redirect:/admin/reports";
        }
        var saved = savedReportService.update(id, request);
        auditLogService.logAction(
                "SAVED_REPORT_UPDATED",
                "SavedReport",
                saved.getId(),
                "Updated saved report: " + saved.getTitle());
        redirectAttributes.addFlashAttribute("success", "Report \"" + saved.getTitle() + "\" updated.");
        return "redirect:/admin/reports";
    }

    @PostMapping("/{id}/delete")
    public String deleteSavedReport(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        var existing = savedReportService.getById(id);
        String title = existing.getTitle();
        savedReportService.delete(id);
        auditLogService.logAction(
                "SAVED_REPORT_DELETED",
                "SavedReport",
                id,
                "Deleted saved report: " + title);
        redirectAttributes.addFlashAttribute("success", "Report \"" + title + "\" deleted.");
        return "redirect:/admin/reports";
    }

    private void enrichSummary(Model model) {
        Map<String, Object> summaryStats = reportService.getSummaryStats();
        model.addAttribute("summaryStats", summaryStats);
        long totalSlots = toLong(summaryStats.get("totalSlots"));
        long availableSlots = toLong(summaryStats.get("availableSlots"));
        double occupancyPercent = totalSlots > 0 ? (totalSlots - availableSlots) * 100.0 / totalSlots : 0.0;
        model.addAttribute("occupancyPercent", occupancyPercent);
    }

    private static long toLong(Object n) {
        if (n == null) {
            return 0L;
        }
        if (n instanceof Number num) {
            return num.longValue();
        }
        return Long.parseLong(n.toString());
    }

    @GetMapping("/revenue")
    public String revenueReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false, defaultValue = "daily") String groupBy,
            Model model) {
        model.addAttribute("revenueData", reportService.getRevenueReport(from, to, groupBy));
        model.addAttribute("totalRevenue", reportService.getTotalRevenue(from, to));
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        return "reports/revenuerepoartpage";
    }

    @GetMapping("/occupancy")
    public String occupancyReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Model model) {
        model.addAttribute("occupancyData", reportService.getOccupancyReport(from, to));
        model.addAttribute("peakHours", reportService.getPeakHours());
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        return "reports/occupancyrepoartpage";
    }

    @GetMapping("/daily-revenue")
    public String dailyRevenueReport(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {
        LocalDate reportDate = (date != null) ? date : LocalDate.now();
        model.addAttribute("dailyRevenue", reportService.getDailyRevenueReport(reportDate));
        model.addAttribute("reportDate", reportDate);
        return "reports/dailyrevenuereport";
    }

    @GetMapping("/export")
    public String exportReport(@RequestParam String type,
                               @RequestParam(required = false) String format) {
        return "redirect:/admin/reports";
    }
}
