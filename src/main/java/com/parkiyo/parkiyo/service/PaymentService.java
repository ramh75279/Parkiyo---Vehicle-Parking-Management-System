package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.enums.PaymentStatus;
import com.parkiyo.parkiyo.enums.NotificationType;
import com.parkiyo.parkiyo.exception.InsufficientBalanceException;
import com.parkiyo.parkiyo.exception.PaymentException;
import com.parkiyo.parkiyo.exception.ResourceNotFoundException;
import com.parkiyo.parkiyo.model.*;
import com.parkiyo.parkiyo.repository.PaymentRepository;
import com.parkiyo.parkiyo.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final WalletRepository walletRepository;
    private final WalletService walletService;
    private final AuditLogService auditLogService;
    private final NotificationService notificationService;

    @PersistenceContext
    private final EntityManager entityManager;

    // ====================== Read Operations ======================

    public Payment getPaymentById(Long id, String email) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));

        if (!payment.getUser().getEmail().equalsIgnoreCase(email)) {
            throw new PaymentException("You are not authorized to access this payment.");
        }
        return payment;
    }

    public List<Payment> getUserPaymentHistory(String email) {
        return paymentRepository.findByUserEmail(email);
    }

    public BigDecimal getUserTotalSpent(String email) {
        return paymentRepository.findByUserEmail(email).stream()
                .filter(p -> p.getStatus() == PaymentStatus.SUCCESS)
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Receipt> getUserReceipts(String email) {
        return paymentRepository.findByUserEmailAndStatus(email, PaymentStatus.SUCCESS).stream()
                .map(Payment::getReceipt)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public Receipt getLatestReceipt(String email) {
        return paymentRepository.findByUserEmailAndStatus(email, PaymentStatus.SUCCESS).stream()
                .filter(payment -> payment.getReceipt() != null)
                .max(Comparator.comparing(p -> p.getPaidAt() != null ? p.getPaidAt() : LocalDateTime.MIN))
                .map(Payment::getReceipt)
                .orElseThrow(() -> new ResourceNotFoundException("No receipt found for user: " + email));
    }

    public Receipt getReceipt(Long paymentId, String email) {
        if (paymentId == null) {
            return getLatestReceipt(email);
        }
        Payment payment = getPaymentById(paymentId, email);

        if (payment.getReceipt() == null) {
            throw new ResourceNotFoundException("Receipt not found for this payment.");
        }
        return payment.getReceipt();
    }

    public Receipt getAdminReceipt(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));

        if (payment.getReceipt() == null) {
            throw new ResourceNotFoundException("Receipt not found for this payment.");
        }
        return payment.getReceipt();
    }

    public List<Payment> getAllPayments(String status, String dateFrom, String dateTo) {
        if (status != null && !status.isBlank()) {
            try {
                return paymentRepository.findByStatus(PaymentStatus.valueOf(status.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new PaymentException("Invalid payment status: " + status);
            }
        }
        return paymentRepository.findAll();
    }

    public BigDecimal getTotalRevenue() {
        Double total = paymentRepository.sumTotalRevenue();
        return total != null ? BigDecimal.valueOf(total) : BigDecimal.ZERO;
    }

    // ====================== Critical Write Operations ======================

    @Transactional
    public void initiatePayment(Long paymentId, String email) {
        // Pessimistic lock on Payment
        Payment payment = entityManager.find(Payment.class, paymentId, LockModeType.PESSIMISTIC_WRITE);
        if (payment == null) {
            throw new ResourceNotFoundException("Payment not found with id: " + paymentId);
        }

        if (!payment.getUser().getEmail().equalsIgnoreCase(email)) {
            throw new PaymentException("Unauthorized: You cannot process this payment.");
        }

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new PaymentException("Only PENDING payments can be initiated. Current status: " + payment.getStatus());
        }

        // Pessimistic lock on Wallet
        Wallet wallet = walletRepository.findByUserEmail(email)
                .map(w -> entityManager.find(Wallet.class, w.getId(), LockModeType.PESSIMISTIC_WRITE))
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user: " + email));

        if (wallet.getBalance().compareTo(payment.getAmount()) < 0) {
            throw new InsufficientBalanceException(
                    String.format("Insufficient balance. Required: %s, Available: %s",
                            payment.getAmount(), wallet.getBalance())
            );
        }

        // Deduct balance
        BigDecimal newBalance = wallet.getBalance().subtract(payment.getAmount());
        wallet.setBalance(newBalance);

        // Create wallet transaction
        WalletTransaction walletTransaction = WalletTransaction.builder()
                .wallet(wallet)
                .type("DEBIT")
                .amount(payment.getAmount())
                .balanceAfter(newBalance)
                .description("Payment for " + payment.getTransactionCode())
                .payment(payment)
                .build();

        if (wallet.getTransactions() == null) {
            wallet.setTransactions(new ArrayList<>());
        }
        wallet.getTransactions().add(walletTransaction);

        // Update payment status
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaymentMethod("Parkiyo Wallet");
        payment.setPaidAt(LocalDateTime.now());

        // Build and attach receipt
        Receipt receipt = buildReceiptSnapshot(payment);
        payment.setReceipt(receipt);

        // Save
        walletRepository.save(wallet);
        paymentRepository.save(payment);

        // Notifications & Audit
        notificationService.createNotification(
                payment.getUser(),
                NotificationType.PAYMENT,
                "Payment Successful",
                "Payment " + payment.getTransactionCode() + " completed successfully.",
                "/payments/history"
        );

        auditLogService.logAction(
                "PAYMENT_COMPLETED",
                email,
                "Payment",
                payment.getId(),
                "Wallet payment completed for transaction " + payment.getTransactionCode(),
                null, null, null
        );
    }

    @Transactional
    public void refundPayment(Long id) {
        Payment payment = entityManager.find(Payment.class, id, LockModeType.PESSIMISTIC_WRITE);
        if (payment == null) {
            throw new ResourceNotFoundException("Payment not found with id: " + id);
        }

        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new PaymentException("Only SUCCESS payments can be refunded.");
        }

        Wallet wallet = walletRepository.findByUserEmail(payment.getUser().getEmail())
                .map(w -> entityManager.find(Wallet.class, w.getId(), LockModeType.PESSIMISTIC_WRITE))
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));

        BigDecimal newBalance = wallet.getBalance().add(payment.getAmount());
        wallet.setBalance(newBalance);

        WalletTransaction walletTransaction = WalletTransaction.builder()
                .wallet(wallet)
                .type("CREDIT")
                .amount(payment.getAmount())
                .balanceAfter(newBalance)
                .description("Refund for " + payment.getTransactionCode())
                .payment(payment)
                .build();

        if (wallet.getTransactions() == null) {
            wallet.setTransactions(new ArrayList<>());
        }
        wallet.getTransactions().add(walletTransaction);

        payment.setStatus(PaymentStatus.REFUNDED);
        payment.setRefundedAt(LocalDateTime.now());

        walletRepository.save(wallet);
        paymentRepository.save(payment);

        notificationService.createNotification(
                payment.getUser(),
                NotificationType.PAYMENT,
                "Payment Refunded",
                "Refund for payment " + payment.getTransactionCode() + " has been processed.",
                "/payments/history"
        );

        auditLogService.logAction(
                "PAYMENT_REFUNDED",
                payment.getUser().getEmail(),
                "Payment",
                payment.getId(),
                "Refund completed for transaction " + payment.getTransactionCode(),
                null, null, null
        );
    }

    private Receipt buildReceiptSnapshot(Payment payment) {
        // Your original method - kept almost as-is with minor safety improvements
        ParkingRecord parkingRecord = payment.getParkingRecord();
        Reservation reservation = payment.getReservation();

        String plate = null;
        String vehicleModel = null;
        String slotCode = null;
        String zone = null;
        LocalDateTime arrival = null;
        LocalDateTime departure = null;
        Integer durationMinutes = null;

        if (parkingRecord != null) {
            if (parkingRecord.getVehicle() != null) {
                plate = parkingRecord.getVehicle().getLicensePlate();
                String make = parkingRecord.getVehicle().getMake();
                String model = parkingRecord.getVehicle().getModel();
                vehicleModel = (make != null && model != null) ? make + " " + model :
                        (model != null ? model : make);
            }
            if (parkingRecord.getSlot() != null) {
                slotCode = parkingRecord.getSlot().getSlotNumber();
                zone = parkingRecord.getSlot().getZone();
            }
            arrival = parkingRecord.getEntryTime();
            departure = parkingRecord.getExitTime();
            durationMinutes = parkingRecord.getDurationMinutes();
        }

        if (reservation != null) {
            if (plate == null && reservation.getVehicle() != null) {
                plate = reservation.getVehicle().getLicensePlate();
                String make = reservation.getVehicle().getMake();
                String model = reservation.getVehicle().getModel();
                vehicleModel = (make != null && model != null) ? make + " " + model :
                        (model != null ? model : make);
            }
            if (slotCode == null && reservation.getSlot() != null) {
                slotCode = reservation.getSlot().getSlotNumber();
                zone = reservation.getSlot().getZone();
            }
            if (arrival == null) arrival = reservation.getStartTime();
            if (departure == null) departure = reservation.getEndTime();
        }

        if (durationMinutes == null && arrival != null && departure != null && !departure.isBefore(arrival)) {
            durationMinutes = (int) Duration.between(arrival, departure).toMinutes();
        }

        String durationText = "-";
        if (durationMinutes != null && durationMinutes > 0) {
            long hours = durationMinutes / 60;
            long minutes = durationMinutes % 60;
            durationText = (minutes == 0) ? hours + "h" : hours + "h " + minutes + "m";
        }

        String firstName = payment.getUser() != null ? payment.getUser().getFirstName() : null;
        String lastName = payment.getUser() != null ? payment.getUser().getLastName() : null;
        String customerName = ((firstName == null ? "" : firstName) + " " + (lastName == null ? "" : lastName)).trim();
        if (customerName.isBlank()) {
            customerName = payment.getUser() != null ? payment.getUser().getEmail() : "-";
        }

        return Receipt.builder()
                .payment(payment)
                .receiptNumber("RCP-" + payment.getTransactionCode())
                .transactionId(payment.getTransactionCode())
                .paymentDate(payment.getPaidAt())
                .customerName(customerName)
                .customerEmail(payment.getUser() != null ? payment.getUser().getEmail() : null)
                .plate(plate)
                .vehicleLicensePlate(plate)
                .vehicleModel(vehicleModel)
                .slotCode(slotCode)
                .slotNumber(slotCode)
                .zone(zone)
                .sessionType(payment.getReservation() != null ? "Advance Reservation" : "Walk-in Parking")
                .date(payment.getPaidAt() != null ? payment.getPaidAt().toLocalDate() : null)
                .arrival(arrival)
                .departure(departure)
                .entryTime(arrival)
                .exitTime(departure)
                .duration(durationText)
                .parkingDuration(durationMinutes)
                .billingBreakdown(durationText)
                .discount(null)
                .subtotal(payment.getAmount())
                .tax(BigDecimal.ZERO)
                .total(payment.getAmount())
                .amountPaid(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .build();
    }
}