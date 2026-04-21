package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.model.AuditLog;
import com.parkiyo.parkiyo.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public List<Map<String, Object>> getLogs(String action, String user,
                                             String dateFrom, String dateTo) {
        LocalDateTime from = parseStartOfDay(dateFrom);
        LocalDateTime to = parseEndOfDay(dateTo);

        return auditLogRepository.findAllByOrderByCreatedAtDesc().stream()
                .filter(log -> action == null || action.isBlank() ||
                        (log.getAction() != null && log.getAction().equalsIgnoreCase(action)))
                .filter(log -> user == null || user.isBlank() || matchesUser(log, user))
                .filter(log -> from == null || (log.getCreatedAt() != null && !log.getCreatedAt().isBefore(from)))
                .filter(log -> to == null || (log.getCreatedAt() != null && !log.getCreatedAt().isAfter(to)))
                .map(this::toSummaryMap)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getLogById(Long id) {
        AuditLog log = auditLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Audit log not found."));
        return toDetailMap(log);
    }

    public List<String> getAllActionTypes() {
        return auditLogRepository.findDistinctByOrderByActionAsc().stream()
                .map(AuditLog::getAction)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    private boolean matchesUser(AuditLog log, String userFilter) {
        String filter = userFilter.toLowerCase(Locale.ROOT);
        if (log.getPerformedBy() == null) {
            return false;
        }

        String firstName = log.getPerformedBy().getFirstName();
        String lastName = log.getPerformedBy().getLastName();
        String email = log.getPerformedBy().getEmail();
        String fullName = ((firstName == null ? "" : firstName) + " " + (lastName == null ? "" : lastName)).trim();

        return (fullName.toLowerCase(Locale.ROOT).contains(filter)) ||
            (email != null && email.toLowerCase(Locale.ROOT).contains(filter));
    }

    private LocalDateTime parseStartOfDay(String dateFrom) {
        if (dateFrom == null || dateFrom.isBlank()) {
            return null;
        }
        return LocalDate.parse(dateFrom).atStartOfDay();
    }

    private LocalDateTime parseEndOfDay(String dateTo) {
        if (dateTo == null || dateTo.isBlank()) {
            return null;
        }
        return LocalDate.parse(dateTo).atTime(23, 59, 59);
    }

    private Map<String, Object> toSummaryMap(AuditLog log) {
        String userName = "System";
        String userEmail = "";
        String role = "SYSTEM";

        if (log.getPerformedBy() != null) {
            String firstName = log.getPerformedBy().getFirstName();
            String lastName = log.getPerformedBy().getLastName();
            userName = ((firstName == null ? "" : firstName) + " " + (lastName == null ? "" : lastName)).trim();
            if (userName.isBlank()) {
                userName = log.getPerformedBy().getEmail();
            }
            userEmail = log.getPerformedBy().getEmail();
            role = log.getPerformedBy().getRole() != null ? log.getPerformedBy().getRole().name() : "UNKNOWN";
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", log.getId());
        result.put("createdAt", log.getCreatedAt());
        result.put("action", log.getAction());
        result.put("entityType", log.getEntityType());
        result.put("entityId", log.getEntityId());
        result.put("description", log.getDescription());
        result.put("ipAddress", log.getIpAddress());
        result.put("userName", userName);
        result.put("userEmail", userEmail);
        result.put("role", role);
        return result;
    }

    private Map<String, Object> toDetailMap(AuditLog log) {
        String userName = "System";
        String userEmail = "";
        String role = "SYSTEM";

        if (log.getPerformedBy() != null) {
            String firstName = log.getPerformedBy().getFirstName();
            String lastName = log.getPerformedBy().getLastName();
            userName = ((firstName == null ? "" : firstName) + " " + (lastName == null ? "" : lastName)).trim();
            if (userName.isBlank()) {
                userName = log.getPerformedBy().getEmail();
            }
            userEmail = log.getPerformedBy().getEmail();
            role = log.getPerformedBy().getRole() != null ? log.getPerformedBy().getRole().name() : "UNKNOWN";
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", log.getId());
        result.put("createdAt", log.getCreatedAt());
        result.put("action", log.getAction());
        result.put("entityType", log.getEntityType());
        result.put("entityId", log.getEntityId());
        result.put("description", log.getDescription());
        result.put("changeDetails", log.getChangeDetails());
        result.put("ipAddress", log.getIpAddress());
        result.put("userAgent", log.getUserAgent());
        result.put("userName", userName);
        result.put("userEmail", userEmail);
        result.put("role", role);
        return result;
    }
}
