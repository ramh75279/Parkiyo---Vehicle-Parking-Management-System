package com.parkiyo.parkiyo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "receipts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false, unique = true)
    private Payment payment;

    // Durable receipt snapshot fields (avoid view dependence on lazy object graph)
    private String receiptNumber;
    private String transactionId;
    private LocalDateTime paymentDate;

    private String customerName;
    private String customerEmail;

    private String plate;
    private String vehicleModel;
    private String vehicleLicensePlate;

    private String slotCode;
    private String slotNumber;
    private String zone;

    private String sessionType;
    private LocalDate date;
    private LocalDateTime arrival;
    private LocalDateTime departure;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private String duration;
    private Integer parkingDuration;

    private String billingBreakdown;
    private String discount;

    @Column(precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(precision = 10, scale = 2)
    private BigDecimal tax;

    @Column(precision = 10, scale = 2)
    private BigDecimal total;

    @Column(precision = 10, scale = 2)
    private BigDecimal amountPaid;

    private String paymentMethod;

    @CreationTimestamp
    private LocalDateTime createdAt;
}