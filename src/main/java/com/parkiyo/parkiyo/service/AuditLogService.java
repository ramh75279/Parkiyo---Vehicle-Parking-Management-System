package com.parkiyo.parkiyo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    // Simplified audit log for presentation
    // In production: create AuditLog entity + repository

    public List<Map<String, Object>> getLogs(String action, String user,
                                             String dateFrom, String dateTo) {
        // Returns empty list until AuditLog entity is wired up
        return List.of();
    }

    public Map<String, Object> getLogById(Long id) {
        return Map.of("id", id, "action", "N/A", "details", "Audit log not yet persisted.");
    }

    public List<String> getAllActionTypes() {
        return List.of("LOGIN", "LOGOUT", "ENTRY", "EXIT", "PAYMENT",
                "RESERVATION", "USER_CREATED", "USER_UPDATED", "SLOT_CREATED");
    }
}
