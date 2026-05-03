package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.dto.ExitRequest;
import com.parkiyo.parkiyo.service.ExitService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/exit")
public class ExitController {

    private final ExitService exitService;

    @GetMapping
    public String exitPage(Model model) {
        return "parking/exitvehicle";   // your thymeleaf template
    }

    @PostMapping("/process")
    public String processExit(@ModelAttribute ExitRequest request,
                              Authentication auth,
                              Model model) {
        try {
            exitService.processExit(request, auth.getName());
            model.addAttribute("success", "Vehicle exited successfully!");
            return "redirect:/parking?success=true";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "parking/exitvehicle";
        }
    }
}