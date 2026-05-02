package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.enums.PaymentStatus;
import com.parkiyo.parkiyo.enums.NotificationType;
import com.parkiyo.parkiyo.model.Payment;
import com.parkiyo.parkiyo.model.Receipt;
import com.parkiyo.parkiyo.repository.PaymentRepository;
import com.parkiyo.parkiyo.service.AuditLogService;
import com.parkiyo.parkiyo.service.NotificationService;
import com.parkiyo.parkiyo.service.WalletService;
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
    private final WalletService walletService;
    private final AuditLogService auditLogService;
    private final NotificationService notificationService;

    // ==================== BASIC GETTERS ====================

    public Payment getPendingPayment(Long id, String email) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found."));

        if (payment.getUser() == null ||
                !payment.getUser().getEmail().equalsIgnoreCase(email)) {
            throw new RuntimeException("You are not authorized to view this payment.");
        }
        return payment;
    }

    public Payment getPaymentById(Long id, String email) {
        return getPendingPayment(id, email);
    }

    public Receipt getReceipt(Long paymentId, String email) {
        Payment payment = getPendingPayment(paymentId, email);
        return payment.getReceipt();
    }

    /**
     * ✅ NEW METHOD - For Admin (no user ownership check)
     */
    public Receipt getAdminReceipt(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found."));
        return payment.getReceipt();
    }

    public List<Payment> getUserPaymentHistory(String email) {
        return paymentRepository.findByUserEmail(email);
    }

    public List<Payment> getAllPaymentHistory() {
        return paymentRepository.findAll();
    }

    public Payment getLatestSuccessfulPayment(String email) {
        return paymentRepository.findTopByUserEmailAndStatusOrderByPaidAtDesc(email, PaymentStatus.SUCCESS)
                .orElse(null);
    }

    public Receipt getLatestReceipt(String email) {
        Payment latestPayment = getLatestSuccessfulPayment(email);
        if (latestPayment != null && latestPayment.getReceipt() != null) {
            return latestPayment.getReceipt();
        }
        return null;
    }

    public BigDecimal getUserTotalSpent(String email) {
        return paymentRepository.findByUserEmail(email).stream()
                .filter(p -> p.getStatus() == PaymentStatus.SUCCESS)
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // ==================== PAYMENT PROCESSING ====================

    @Transactional
    public void initiatePayment(Long paymentId, String userEmail) {
        initiateWalletPayment(paymentId, userEmail);
    }

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

        if (payment.getUser() == null ||
                !payment.getUser().getEmail().equalsIgnoreCase(userEmail)) {
            throw new RuntimeException("You are not authorized.");
        }

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new RuntimeException("Only pending payments can be processed.");
        }

        BigDecimal balance = walletService.getBalance(userEmail);
        if (balance.compareTo(payment.getAmount()) < 0) {
            throw new RuntimeException("Insufficient wallet balance.");
        }

        walletService.deductBalance(userEmail, payment.getAmount(),
                "Payment for " + payment.getTransactionCode());

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
        if (payment.getReceipt() != null) {
            return; // Receipt already exists
        }

        Receipt receipt = Receipt.builder()
                .payment(payment)
                .receiptNumber("RCP-" + payment.getTransactionCode())
                .transactionId(payment.getTransactionCode())
                .paymentDate(payment.getPaidAt() != null ? payment.getPaidAt() : LocalDateTime.now())
                .customerName(payment.getUser() != null ? payment.getUser().getFullName() : "N/A")
                .customerEmail(payment.getUser() != null ? payment.getUser().getEmail() : "N/A")
                .plate(payment.getParkingRecord() != null &&
                        payment.getParkingRecord().getVehicle() != null ?
                        payment.getParkingRecord().getVehicle().getLicensePlate() : "N/A")
                .amountPaid(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod() != null ? payment.getPaymentMethod() : "Unknown")
                .build();

        payment.setReceipt(receipt);
    }

    // ==================== ADMIN METHODS ====================

    public List<Payment> getAllPayments(String status, String dateFrom, String dateTo) {
        if (status != null && !status.isBlank()) {
            try {
                return paymentRepository.findByStatus(PaymentStatus.valueOf(status.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid payment status: " + status);
            }
        }
        return paymentRepository.findAll();
    }

    public BigDecimal getTotalRevenue() {
        Double total = paymentRepository.sumTotalRevenue();
        return total != null ? BigDecimal.valueOf(total) : BigDecimal.ZERO;
    }

    @Transactional
    public void refundPayment(Long id) {
        throw new UnsupportedOperationException("Refund functionality not implemented yet.");
    }
}