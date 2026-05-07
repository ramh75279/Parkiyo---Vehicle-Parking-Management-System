package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @PostMapping("/upload-profile-picture")
    public String uploadProfilePicture(@RequestParam("profilePicture") MultipartFile file,
                                       Authentication auth,
                                       RedirectAttributes redirectAttributes) {
        try {
            userService.updateProfilePicture(auth.getName(), file);
            redirectAttributes.addFlashAttribute("success", "Profile picture updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to upload: " + e.getMessage());
        }
        return "redirect:/account/settings";
    }
}