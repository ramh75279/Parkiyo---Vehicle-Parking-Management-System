package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.model.User;
import com.parkiyo.parkiyo.service.NotificationService;
import com.parkiyo.parkiyo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(annotations = Controller.class)
@RequiredArgsConstructor
public class GlobalModelAttributes {

    private final UserService userService;
    private final NotificationService notificationService;

    @ModelAttribute("currentUser")
    public User currentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return userService.getUserByEmail(authentication.getName());
    }

    @ModelAttribute("currentUserUnreadNotifications")
    public long currentUserUnreadNotifications(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return 0;
        }
        return notificationService.getUnreadCount(authentication.getName());
    }
}
