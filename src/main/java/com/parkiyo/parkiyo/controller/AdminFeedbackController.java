package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.enums.FeedbackCategory;
import com.parkiyo.parkiyo.enums.FeedbackPriority;
import com.parkiyo.parkiyo.enums.FeedbackReportStatus;
import com.parkiyo.parkiyo.model.FeedbackReport;
import com.parkiyo.parkiyo.service.FeedbackReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/feedback")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminFeedbackController {

    private final FeedbackReportService feedbackReportService;

    @GetMapping({"", "/"})
    public String dashboard(@RequestParam(required = false) String status,
                            @RequestParam(required = false) String category,
                            Model model) {
        Map<String, Object> stats = feedbackReportService.adminDashboardStats();
        model.addAllAttributes(stats);
        List<FeedbackReport> reports = feedbackReportService.listForAdmin(status, category);
        model.addAttribute("reports", reports);
        model.addAttribute("statusFilter", status != null ? status : "ALL");
        model.addAttribute("categoryFilter", category != null ? category : "ALL");
        model.addAttribute("statuses", Arrays.asList(FeedbackReportStatus.values()));
        model.addAttribute("categories", Arrays.asList(FeedbackCategory.values()));
        return "admin/feedback/dashboard";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            FeedbackReport report = feedbackReportService.getForAdmin(id);
            model.addAttribute("report", report);
            model.addAttribute("statuses", Arrays.asList(FeedbackReportStatus.values()));
            model.addAttribute("priorities", Arrays.asList(FeedbackPriority.values()));
            return "admin/feedback/detail";
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/feedback";
        }
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam FeedbackReportStatus status,
                         @RequestParam FeedbackPriority priority,
                         @RequestParam(required = false) String adminResponse,
                         RedirectAttributes ra) {
        try {
            feedbackReportService.updateByAdmin(id, status, priority, adminResponse);
            ra.addFlashAttribute("success", "Report updated.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/feedback/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            feedbackReportService.deleteByAdmin(id);
            ra.addFlashAttribute("success", "Report removed.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/feedback";
    }

    @GetMapping("/analytics")
    public String analytics(Model model) {
        model.addAllAttributes(feedbackReportService.adminDashboardStats());
        return "admin/feedback/analytics";
    }
}
