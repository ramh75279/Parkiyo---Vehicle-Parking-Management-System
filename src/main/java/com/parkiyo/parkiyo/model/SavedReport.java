package com.parkiyo.parkiyo.model;

import com.parkiyo.parkiyo.enums.SavedReportPeriod;
import com.parkiyo.parkiyo.enums.SavedReportStatus;
import com.parkiyo.parkiyo.enums.SavedReportType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "saved_reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavedReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private SavedReportType reportType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private SavedReportStatus status;

    /** Viewing / aggregation cadence (daily, monthly, yearly) — independent of {@link #reportType}. */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private SavedReportPeriod period = SavedReportPeriod.DAILY;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    void normalizePeriod() {
        if (period == null) {
            period = SavedReportPeriod.DAILY;
        }
    }
}
