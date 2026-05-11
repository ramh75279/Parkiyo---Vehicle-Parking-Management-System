package com.parkiyo.parkiyo.model;

import com.parkiyo.parkiyo.enums.FeedbackCategory;
import com.parkiyo.parkiyo.enums.FeedbackPriority;
import com.parkiyo.parkiyo.enums.FeedbackReportStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "feedback_reports", indexes = {
        @Index(name = "idx_feedback_user", columnList = "user_id"),
        @Index(name = "idx_feedback_status", columnList = "status"),
        @Index(name = "idx_feedback_category", columnList = "category"),
        @Index(name = "idx_feedback_created", columnList = "created_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private FeedbackCategory category;

    /** 1–5 stars; optional for non-review categories */
    @Column
    private Integer rating;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private FeedbackReportStatus status = FeedbackReportStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private FeedbackPriority priority = FeedbackPriority.MEDIUM;

    @Column(columnDefinition = "TEXT")
    private String adminResponse;

    /** Relative to upload root, e.g. {@code feedback/uuid.jpg} */
    @Column(length = 500)
    private String attachmentPath;

    private LocalDateTime respondedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
