package com.parkiyo.parkiyo.model;

import com.parkiyo.parkiyo.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String transactionCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_record_id")
    private ParkingRecord parkingRecord;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

    private LocalDateTime paidAt;

    private LocalDateTime refundedAt;
    private String refundReason;

    @Column(name = "paid_by", length = 150)
    private String paidBy;           // Operator who took cash payment

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Receipt receipt;

    /** When exit fee was reduced or waived using an active parking pass */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_pass_id")
    private UserPass userPass;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // ==================== HELPER METHODS ====================

    public boolean isSuccessful() {
        return status == PaymentStatus.SUCCESS;
    }

    public boolean isPending() {
        return status == PaymentStatus.PENDING;
    }

    public boolean isRefunded() {
        return refundedAt != null;
    }
}