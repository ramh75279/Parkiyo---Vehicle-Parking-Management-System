package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.model.User;
import com.parkiyo.parkiyo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributes {

    private final UserService userService;

    @ModelAttribute("currentUser")
    public User currentUser(Authentication authentication) {

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {
            return null;
        }

        try {
            return userService.getUserByEmail(authentication.getName());
        } catch (Exception e) {
            System.err.println("Failed to load currentUser: " + authentication.getName());
            e.printStackTrace();
            return null;
        }
    }
}