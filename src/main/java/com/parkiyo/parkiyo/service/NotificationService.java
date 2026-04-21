package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.enums.NotificationType;
import com.parkiyo.parkiyo.model.Notification;
import com.parkiyo.parkiyo.model.User;
import com.parkiyo.parkiyo.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

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
}
