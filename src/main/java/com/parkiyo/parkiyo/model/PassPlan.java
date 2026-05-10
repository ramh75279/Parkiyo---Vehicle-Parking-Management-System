package com.parkiyo.parkiyo.model;

import com.parkiyo.parkiyo.enums.PassPlanCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Admin-managed catalog of parking subscriptions (Weekly, Monthly, VIP, etc.).
 */
@Entity
@Table(name = "pass_plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PassPlanCategory category;

    /** Rs. purchase price (charged from wallet when user buys) */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /** Number of calendar days the pass remains valid from purchase */
    @Column(nullable = false)
    private int durationDays;

    /**
     * Zone label must match {@link ParkingSlot#getZone()} for the benefit to apply,
     * unless this is null/blank or "ALL" (any zone).
     */
    @Column(length = 120)
    private String zone;

    @Column(nullable = false)
    @Builder.Default
    private boolean unlimitedParking = false;

    /** 0–100; ignored when unlimitedParking is true */
    @Column(nullable = false, precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal discountPercent = BigDecimal.ZERO;

    /** Soft-disable plan from catalog without deleting history */
    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    @Column(nullable = false)
    @Builder.Default
    private int displayOrder = 0;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
