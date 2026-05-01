package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.enums.PaymentStatus;
import com.parkiyo.parkiyo.enums.NotificationType;
import com.parkiyo.parkiyo.model.ParkingRecord;
import com.parkiyo.parkiyo.model.Payment;
import com.parkiyo.parkiyo.model.Receipt;
import com.parkiyo.parkiyo.repository.PaymentRepository;
import com.parkiyo.parkiyo.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final WalletRepository walletRepository;
    private final WalletService walletService;
    private final AuditLogService auditLogService;
    private final NotificationService notificationService;

    // ==================== BASIC GETTERS ====================

    public Payment getPendingPayment(Long id, String email) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found."));
        // Basic ownership check
        if (!payment.getUser().getEmail().equalsIgnoreCase(email)) {
            throw new RuntimeException("You are not authorized to view this payment.");
        }
        return payment;
    }

    public Payment getPaymentById(Long id, String email) {
        return getPendingPayment(id, email); // reuse with check
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

    // ==================== PAYMENT PROCESSING ====================

    @Transactional
    public void completeCashPayment(Long paymentId, String operatorEmail) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found."));

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new RuntimeException("This payment is not pending.");
        }

        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaymentMethod("CASH");
        payment.setPaidAt(LocalDateTime.now());
        payment.setPaidBy(operatorEmail);

        // Update related parking record status if exists
        if (payment.getParkingRecord() != null) {
            payment.getParkingRecord().setPaid(true);
        }

        createReceipt(payment);
        paymentRepository.save(payment);

        notificationService.createNotification(
                payment.getUser(),
                NotificationType.PAYMENT,
                "Payment Successful",
                "Cash payment " + payment.getTransactionCode() + " completed.",
                "/payments/history"
        );

        auditLogService.logAction("PAYMENT_CASH", operatorEmail, "Payment", payment.getId(),
                "Cash payment completed for " + payment.getTransactionCode(), null, null, null);
    }

    @Transactional
    public void initiateWalletPayment(Long paymentId, String userEmail) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found."));

        if (!payment.getUser().getEmail().equalsIgnoreCase(userEmail)) {
            throw new RuntimeException("You are not authorized.");
        }

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new RuntimeException("Only pending payments can be processed.");
        }

        BigDecimal balance = walletService.getBalance(userEmail);
        if (balance.compareTo(payment.getAmount()) < 0) {
            throw new RuntimeException("Insufficient wallet balance.");
        }

        // Deduct from wallet
        walletService.deductBalance(userEmail, payment.getAmount(), "Payment for " + payment.getTransactionCode());

        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaymentMethod("Parkiyo Wallet");
        payment.setPaidAt(LocalDateTime.now());

        createReceipt(payment);
        paymentRepository.save(payment);

        notificationService.createNotification(
                payment.getUser(),
                NotificationType.PAYMENT,
                "Payment Successful",
                "Wallet payment " + payment.getTransactionCode() + " completed.",
                "/payments/history"
        );
    }

    private void createReceipt(Payment payment) {
        // Simplified receipt creation
        Receipt receipt = Receipt.builder()
                .payment(payment)
                .receiptNumber("RCP-" + payment.getTransactionCode())
                .transactionId(payment.getTransactionCode())
                .paymentDate(payment.getPaidAt())
                .customerName(payment.getUser().getFullName())
                .customerEmail(payment.getUser().getEmail())
                .plate(payment.getParkingRecord() != null ?
                        payment.getParkingRecord().getVehicle().getLicensePlate() : null)
                .amountPaid(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .build();

        payment.setReceipt(receipt);
    }

    // ==================== ADMIN & OTHER ====================

    public List<Payment> getAllPayments(String status, String dateFrom, String dateTo) {
        if (status != null && !status.isBlank()) {
            return paymentRepository.findByStatus(PaymentStatus.valueOf(status.toUpperCase()));
        }
        return paymentRepository.findAll();
    }

    public BigDecimal getTotalRevenue() {
        Double total = paymentRepository.sumTotalRevenue();
        return total != null ? BigDecimal.valueOf(total) : BigDecimal.ZERO;
    }

    @Transactional
    public void refundPayment(Long id) {
        // ... (keep your existing refund logic or simplify later)
        // For now, you can keep your original refundPayment method
    }
}