package com.parkiyo.parkiyo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The user who performed the action
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performed_by_id")
    private User performedBy;

    // e.g. USER_CREATED, SLOT_DELETED, PAYMENT_REFUNDED
    @Column(nullable = false)
    private String action;

    // The entity type affected e.g. "User", "ParkingSlot"
    private String entityType;

    // The ID of the affected entity
    private Long entityId;

    // Human-readable description
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    // JSON snapshot of changes (before/after)
    @Column(columnDefinition = "TEXT")
    private String changeDetails;

    private String ipAddress;
    private String userAgent;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
