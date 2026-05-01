package com.parkiyo.parkiyo.model;

import com.parkiyo.parkiyo.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "parking_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id")
    private ParkingSlot parkingSlot;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "amount_charged", precision = 10, scale = 2)
    private BigDecimal amountCharged;

    @Column(name = "is_paid", nullable = false)
    private boolean paid = false;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    private LocalDateTime entryTime;
    private LocalDateTime exitTime;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "exit_operator", length = 100)
    private String exitOperator;

    // ✅ NEW FIELD - Added for entryOperator
    @Column(name = "entry_operator", length = 100)
    private String entryOperator;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Relationship with Payment
    @OneToOne(mappedBy = "parkingRecord", fetch = FetchType.LAZY)
    private Payment payment;

    // ==================== CONVENIENCE METHODS ====================

    public ParkingSlot getSlot() {
        return this.parkingSlot;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public void setAmountCharged(BigDecimal amountCharged) {
        this.amountCharged = amountCharged;
    }

    public void setExitOperator(String exitOperator) {
        this.exitOperator = exitOperator;
    }

    public void setEntryOperator(String entryOperator) {
        this.entryOperator = entryOperator;
    }

    // ==================== BUILDER CUSTOMIZATION ====================

    public static class ParkingRecordBuilder {
        public ParkingRecordBuilder slot(ParkingSlot slot) {
            this.parkingSlot = slot;
            return this;
        }

        // ✅ Added to fix current error: .entryOperator(...)
        public ParkingRecordBuilder entryOperator(String entryOperator) {
            this.entryOperator = entryOperator;
            return this;
        }
    }
}