package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.enums.FeedbackCategory;
import com.parkiyo.parkiyo.model.FeedbackReport;
import com.parkiyo.parkiyo.service.FeedbackReportService;
import com.parkiyo.parkiyo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

@Controller
@RequestMapping("/feedback")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackReportService feedbackReportService;
    private final UserService userService;

    @GetMapping({"", "/"})
    public String myReports(Authentication auth, Model model) {
        String email = auth.getName();
        model.addAttribute("reports", feedbackReportService.listForUser(email));
        model.addAttribute("currentUser", userService.getUserByEmail(email));
        model.addAttribute("categories", Arrays.asList(FeedbackCategory.values()));
        return "feedback/my-reports";
    }

    @GetMapping("/new")
    public String newReportForm(Authentication auth, Model model) {
        String email = auth.getName();
        model.addAttribute("categories", Arrays.asList(FeedbackCategory.values()));
        model.addAttribute("currentUser", userService.getUserByEmail(email));
        return "feedback/submit";
    }

    @PostMapping
    public String create(@RequestParam String title,
                         @RequestParam String description,
                         @RequestParam String category,
                         @RequestParam(required = false) Integer rating,
                         @RequestParam(value = "attachment", required = false) MultipartFile attachment,
                         Authentication auth,
                         RedirectAttributes ra) {
        try {
            feedbackReportService.create(auth.getName(), title, description, category, rating, attachment);
            ra.addFlashAttribute("success", "Your feedback was submitted. You can track it below.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/feedback";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Authentication auth, Model model, RedirectAttributes ra) {
        try {
            FeedbackReport report = feedbackReportService.getForUser(id, auth.getName());
            if (!feedbackReportService.userMayEditOrDelete(report)) {
                ra.addFlashAttribute("error", "This report can no longer be edited.");
                return "redirect:/feedback";
            }
            model.addAttribute("report", report);
            model.addAttribute("categories", Arrays.asList(FeedbackCategory.values()));
            model.addAttribute("currentUser", userService.getUserByEmail(auth.getName()));
            return "feedback/edit";
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/feedback";
        }
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @RequestParam String title,
                         @RequestParam String description,
                         @RequestParam String category,
                         @RequestParam(required = false) Integer rating,
                         @RequestParam(value = "attachment", required = false) MultipartFile attachment,
                         @RequestParam(value = "removeAttachment", defaultValue = "false") boolean removeAttachment,
                         Authentication auth,
                         RedirectAttributes ra) {
        try {
            feedbackReportService.updateByUser(id, auth.getName(), title, description, category, rating,
                    attachment, removeAttachment);
            ra.addFlashAttribute("success", "Report updated.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/feedback";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Authentication auth, RedirectAttributes ra) {
        try {
            feedbackReportService.deleteByUser(id, auth.getName());
            ra.addFlashAttribute("success", "Report deleted.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/feedback";
    }
}
