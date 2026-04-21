package com.parkiyo.parkiyo.repository;

import com.parkiyo.parkiyo.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserEmailOrderByCreatedAtDesc(String email);

    List<Notification> findByUserEmailAndReadFalse(String email);

    long countByUserEmailAndReadFalse(String email);
}
