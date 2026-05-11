package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.enums.NotificationType;
import com.parkiyo.parkiyo.model.Notification;
import com.parkiyo.parkiyo.model.User;
import com.parkiyo.parkiyo.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public static String formatRelativeTime(LocalDateTime createdAt) {
        if (createdAt == null) {
            return "";
        }
        LocalDateTime now = LocalDateTime.now();
        long minutes = ChronoUnit.MINUTES.between(createdAt, now);
        if (minutes < 1) {
            return "Just now";
        }
        if (minutes < 60) {
            return minutes + " min ago";
        }
        long hours = ChronoUnit.HOURS.between(createdAt, now);
        if (hours < 24) {
            return hours + (hours == 1 ? " hr ago" : " hrs ago");
        }
        long days = ChronoUnit.DAYS.between(createdAt.toLocalDate(), now.toLocalDate());
        if (days == 1) {
            return "Yesterday";
        }
        if (days < 7) {
            return days + " days ago";
        }
        return createdAt.toLocalDate().toString();
    }

    public List<Notification> getUserNotifications(String email) {
        return notificationRepository.findByUserEmailOrderByCreatedAtDesc(email);
    }

    public long getUnreadCount(String email) {
        return notificationRepository.countByUserEmailAndReadFalse(email);
    }

    @Transactional
    public void createNotification(User user,
                                   NotificationType type,
                                   String title,
                                   String message,
                                   String actionUrl) {
        if (user == null) {
            return;
        }

        Notification notification = Notification.builder()
                .user(user)
                .type(type)
                .title(title)
                .message(message)
                .actionUrl(actionUrl)
                .read(false)
                .build();

        notificationRepository.save(notification);
    }

    @Transactional
    public void markAsRead(String userEmail, Long notificationId) {
        Notification n = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found."));
        if (n.getUser() == null || !n.getUser().getEmail().equalsIgnoreCase(userEmail)) {
            throw new IllegalArgumentException("Not allowed.");
        }
        if (!n.isRead()) {
            n.setRead(true);
            n.setReadAt(LocalDateTime.now());
        }
    }

    @Transactional
    public void markAllAsRead(String userEmail) {
        List<Notification> unread = notificationRepository.findByUserEmailAndReadFalse(userEmail);
        LocalDateTime now = LocalDateTime.now();
        for (Notification n : unread) {
            n.setRead(true);
            n.setReadAt(now);
        }
    }
}
