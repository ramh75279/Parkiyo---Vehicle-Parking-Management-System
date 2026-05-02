package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.model.AuditLog;
import com.parkiyo.parkiyo.model.User;
import com.parkiyo.parkiyo.repository.AuditLogRepository;
import com.parkiyo.parkiyo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final UserRepository userRepository;

    @Transactional
    public void logAction(String action, String entityType, Long entityId, String description) {
        logAction(action, resolveCurrentUserEmail(), entityType, entityId, description, null, null, null);
    }

    @Transactional
    public void logAction(String action,
                          String actorEmail,
                          String entityType,
                          Long entityId,
                          String description,
                          String changeDetails,
                          String ipAddress,
                          String userAgent) {
        User actor = null;
        if (actorEmail != null && !actorEmail.isBlank()) {
            actor = userRepository.findByEmail(actorEmail).orElse(null);
        }

        AuditLog log = AuditLog.builder()
                .performedBy(actor)
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .description(description)
                .changeDetails(changeDetails)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .build();

        auditLogRepository.save(log);
    }

    public List<Map<String, Object>> getLogs(String action, String user,
                                             String dateFrom, String dateTo) {
        LocalDateTime from = parseStartOfDay(dateFrom);
        LocalDateTime to = parseEndOfDay(dateTo);

        return auditLogRepository.findAllByOrderByCreatedAtDesc().stream()
                .filter(log -> action == null || action.isBlank() ||
                        Objects.equals(log.getAction(), action))
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
        return auditLogRepository.findDistinctActions();
    }

    private String resolveCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
            return null;
        }
        return authentication.getName();
    }

    private boolean matchesUser(AuditLog log, String userFilter) {
        if (log.getPerformedBy() == null) return false;
        String filter = userFilter.toLowerCase(Locale.ROOT);

        User u = log.getPerformedBy();
        String fullName = (u.getFirstName() != null ? u.getFirstName() : "") + " " +
                (u.getLastName() != null ? u.getLastName() : "");
        fullName = fullName.trim();

        return fullName.toLowerCase(Locale.ROOT).contains(filter) ||
                (u.getEmail() != null && u.getEmail().toLowerCase(Locale.ROOT).contains(filter));
    }

    private LocalDateTime parseStartOfDay(String dateFrom) {
        if (dateFrom == null || dateFrom.isBlank()) return null;
        return LocalDate.parse(dateFrom).atStartOfDay();
    }

    private LocalDateTime parseEndOfDay(String dateTo) {
        if (dateTo == null || dateTo.isBlank()) return null;
        return LocalDate.parse(dateTo).atTime(23, 59, 59);
    }

    private Map<String, Object> toSummaryMap(AuditLog log) {
        return buildMap(log, false);
    }

    private Map<String, Object> toDetailMap(AuditLog log) {
        return buildMap(log, true);
    }

    private Map<String, Object> buildMap(AuditLog log, boolean includeDetails) {
        String userName = "System";
        String userEmail = "";
        String role = "SYSTEM";

        if (log.getPerformedBy() != null) {
            User u = log.getPerformedBy();
            String firstName = u.getFirstName();
            String lastName = u.getLastName();
            userName = ((firstName == null ? "" : firstName) + " " + (lastName == null ? "" : lastName)).trim();
            if (userName.isBlank()) userName = u.getEmail();
            userEmail = u.getEmail();
            role = u.getRole() != null ? u.getRole().name() : "UNKNOWN";
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", log.getId());
        result.put("createdAt", log.getCreatedAt());
        result.put("action", log.getAction());
        result.put("entityType", log.getEntityType());
        result.put("entityId", log.getEntityId());
        result.put("description", log.getDescription());
        result.put("userName", userName);
        result.put("userEmail", userEmail);
        result.put("role", role);

        if (includeDetails) {
            result.put("changeDetails", log.getChangeDetails());
            result.put("ipAddress", log.getIpAddress());
            result.put("userAgent", log.getUserAgent());
        }
        return result;
    }
}