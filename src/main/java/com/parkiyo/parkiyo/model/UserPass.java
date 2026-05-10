package com.parkiyo.parkiyo.model;

import com.parkiyo.parkiyo.enums.UserPassStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * A user's purchased parking pass instance (subscription window).
 */
@Entity
@Table(name = "user_passes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pass_plan_id", nullable = false)
    private PassPlan passPlan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private UserPassStatus status = UserPassStatus.ACTIVE;

    @Column(nullable = false)
    private LocalDate startDate;

    /** Inclusive end date */
    @Column(nullable = false)
    private LocalDate endDate;

    @CreationTimestamp
    private LocalDateTime purchasedAt;
}
