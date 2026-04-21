package com.parkiyo.parkiyo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "parking_records")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ParkingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id", nullable = false)
    private ParkingSlot slot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private LocalDateTime entryTime;

    private LocalDateTime exitTime;

    // Duration in minutes — calculated on exit
    private Integer durationMinutes;

    private BigDecimal amountCharged;

    // Whether this record is linked to an advance reservation
    @OneToOne(mappedBy = "parkingRecord", fetch = FetchType.LAZY)
    private Reservation reservation;

    @OneToOne(mappedBy = "parkingRecord", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Payment payment;

    // Operator who logged the entry (admin username)
    private String entryOperator;
    private String exitOperator;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean active = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
