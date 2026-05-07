package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.enums.PaymentStatus;
import com.parkiyo.parkiyo.enums.NotificationType;
import com.parkiyo.parkiyo.model.Payment;
import com.parkiyo.parkiyo.model.Receipt;
import com.parkiyo.parkiyo.repository.PaymentHistorySpecification;
import com.parkiyo.parkiyo.repository.PaymentRepository;
import com.parkiyo.parkiyo.service.AuditLogService;
import com.parkiyo.parkiyo.service.NotificationService;
import com.parkiyo.parkiyo.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

    public Page<Payment> getUserPaymentHistoryPaginated(String email, Pageable pageable) {
        return paymentRepository.findByUserEmail(email, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Payment> getUserPaymentHistoryFiltered(
            String email,
            String search,
            String statusKey,
            String methodKey,
            LocalDate from,
            LocalDate to,
            Pageable pageable) {
        PaymentStatus st = parseHistoryStatus(statusKey);
        Specification<Payment> spec = PaymentHistorySpecification.forUserHistory(
                email, search, st, methodKey, from, to);
        return paymentRepository.findAll(spec, pageable);
    }

    private static PaymentStatus parseHistoryStatus(String key) {
        if (key == null || key.isBlank() || "ALL".equalsIgnoreCase(key)) {
            return null;
        }
        if ("PAID".equalsIgnoreCase(key)) {
            return PaymentStatus.SUCCESS;
        }
        try {
            return PaymentStatus.valueOf(key.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Transactional(readOnly = true)
    public BigDecimal getUserSpentInCurrentMonth(String email) {
        YearMonth ym = YearMonth.now();
        return paymentRepository.findByUserEmail(email).stream()
                .filter(p -> p.getStatus() == PaymentStatus.SUCCESS)
                .filter(p -> {
                    LocalDateTime t = p.getPaidAt() != null ? p.getPaidAt() : p.getCreatedAt();
                    return t != null && YearMonth.from(t).equals(ym);
                })
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional(readOnly = true)
    public long countSuccessfulPayments(String email) {
        return paymentRepository.findByUserEmail(email).stream()
                .filter(p -> p.getStatus() == PaymentStatus.SUCCESS)
                .count();
    }

    @Transactional(readOnly = true)
    public long countAllUserPayments(String email) {
        return paymentRepository.countByUser_Email(email);
    }

    @Transactional(readOnly = true)
    public BigDecimal getUserAverageSuccessfulPayment(String email) {
        long n = countSuccessfulPayments(email);
        if (n == 0) {
            return BigDecimal.ZERO;
        }
        return getUserTotalSpent(email).divide(BigDecimal.valueOf(n), 2, RoundingMode.HALF_UP);
    }

    /**
     * Last six calendar months including the current month, for bar chart (successful payments only).
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getLastSixMonthlySpendBars(String email) {
        YearMonth end = YearMonth.now();
        List<YearMonth> months = new ArrayList<>();
        for (int i = 5; i >= 0; i--) {
            months.add(end.minusMonths(i));
        }
        Map<YearMonth, BigDecimal> totals = new HashMap<>();
        for (Payment p : paymentRepository.findByUserEmail(email)) {
            if (p.getStatus() != PaymentStatus.SUCCESS) {
                continue;
            }
            LocalDateTime t = p.getPaidAt() != null ? p.getPaidAt() : p.getCreatedAt();
            if (t == null) {
                continue;
            }
            YearMonth ym = YearMonth.from(t);
            if (months.contains(ym)) {
                totals.merge(ym, p.getAmount(), BigDecimal::add);
            }
        }
        BigDecimal max = months.stream()
                .map(ym -> totals.getOrDefault(ym, BigDecimal.ZERO))
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        if (max.compareTo(BigDecimal.ZERO) <= 0) {
            max = BigDecimal.ONE;
        }
        List<Map<String, Object>> out = new ArrayList<>();
        for (YearMonth ym : months) {
            BigDecimal total = totals.getOrDefault(ym, BigDecimal.ZERO);
            int pct = total.multiply(BigDecimal.valueOf(100))
                    .divide(max, 0, RoundingMode.HALF_UP)
                    .intValue();
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("shortLabel", ym.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
            row.put("total", total);
            row.put("heightPct", Math.max(pct, 6));
            row.put("amountLabel", total.compareTo(BigDecimal.ZERO) > 0
                    ? "Rs. " + total.setScale(0, RoundingMode.HALF_UP)
                    : "—");
            out.add(row);
        }
        return out;
    }

    public String getCurrentMonthDisplayLabel() {
        return YearMonth.now().format(DateTimeFormatter.ofPattern("MMM yyyy", Locale.ENGLISH));
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