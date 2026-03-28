package com.parkiyo.controller;

import com.parkiyo.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin/reports")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    // GET /admin/reports  - reports hub
    @GetMapping
    public String reportsHub(Model model) {
        model.addAttribute("summaryStats", reportService.getSummaryStats());
        return "repportshubpage";
    }

    // GET /admin/reports/revenue
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
        return "revenuerepoartpage";
    }

    // GET /admin/reports/occupancy
    @GetMapping("/occupancy")
    public String occupancyReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Model model) {
        model.addAttribute("occupancyData", reportService.getOccupancyReport(from, to));
        model.addAttribute("peakHours", reportService.getPeakHours());
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        return "occupancyrepoartpage";
    }

    // GET /admin/reports/daily-revenue
    @GetMapping("/daily-revenue")
    public String dailyRevenueReport(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {
        LocalDate reportDate = (date != null) ? date : LocalDate.now();
        model.addAttribute("dailyRevenue", reportService.getDailyRevenueReport(reportDate));
        model.addAttribute("reportDate", reportDate);
        return "dailyrevenuereport";
    }

    // GET /admin/reports/export  (CSV/PDF download)
    @GetMapping("/export")
    public String exportReport(@RequestParam String type,
                               @RequestParam(required = false) String format) {
        // Handled via ResponseEntity in a separate @RestController if needed
        return "redirect:/admin/reports";
    }
}
