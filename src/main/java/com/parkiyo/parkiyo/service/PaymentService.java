package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.enums.PaymentStatus;
import com.parkiyo.parkiyo.model.Payment;
import com.parkiyo.parkiyo.model.Receipt;
import com.parkiyo.parkiyo.model.Wallet;
import com.parkiyo.parkiyo.model.WalletTransaction;
import com.parkiyo.parkiyo.repository.PaymentRepository;
import com.parkiyo.parkiyo.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final WalletService walletService;
    private final WalletRepository walletRepository;

    public Payment getPendingPayment(Long id, String email) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found."));
    }

    public Payment getPaymentById(Long id, String email) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found."));
    }

    public Payment getLatestSuccessfulPayment(String email) {
        return paymentRepository
                .findTopByUserEmailAndStatusOrderByPaidAtDesc(email, PaymentStatus.SUCCESS)
                .orElseThrow(() -> new RuntimeException("No successful payment found."));
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

    public List<Payment> getUserReceipts(String email) {
        return paymentRepository.findByUserEmailAndStatus(email, PaymentStatus.SUCCESS);
    }

    public Payment getReceipt(Long paymentId, String email) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Receipt not found."));
    }

    public List<Payment> getAllPayments(String status, String dateFrom, String dateTo) {
        if (status != null && !status.isBlank()) {
            return paymentRepository.findByStatus(PaymentStatus.valueOf(status.toUpperCase()));
        }
        return paymentRepository.findAll();
    }

    public List<Payment> getAllPaymentHistory() {
        return paymentRepository.findAll();
    }

    public BigDecimal getTotalRevenue() {
        Double total = paymentRepository.sumTotalRevenue();
        return total != null ? BigDecimal.valueOf(total) : BigDecimal.ZERO;
    }

    @Transactional
    public void initiatePayment(Long paymentId, String email) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found."));

        if (!payment.getUser().getEmail().equalsIgnoreCase(email)) {
            throw new RuntimeException("You are not allowed to process this payment.");
        }

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new RuntimeException("Only pending payments can be processed.");
        }

        Wallet wallet = walletRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Wallet not found."));

        if (walletService.getBalance(email).compareTo(payment.getAmount()) < 0) {
            throw new RuntimeException("Insufficient wallet balance.");
        }

        BigDecimal newBalance = wallet.getBalance().subtract(payment.getAmount());
        wallet.setBalance(newBalance);

        if (wallet.getTransactions() == null) {
            wallet.setTransactions(new ArrayList<>());
        }

        WalletTransaction walletTransaction = WalletTransaction.builder()
                .wallet(wallet)
                .type("DEBIT")
                .amount(payment.getAmount())
                .balanceAfter(newBalance)
                .description("Payment for " + payment.getTransactionCode())
                .payment(payment)
                .build();
        wallet.getTransactions().add(walletTransaction);

        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaymentMethod("Parkiyo Wallet");
        payment.setPaidAt(LocalDateTime.now());

        // Auto-generate receipt
        Receipt receipt = Receipt.builder().payment(payment).build();
        payment.setReceipt(receipt);
        walletRepository.save(wallet);
        paymentRepository.save(payment);
    }

    @Transactional
    public void refundPayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found."));
        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new RuntimeException("Only successful payments can be refunded.");
        }

        Wallet wallet = walletRepository.findByUserEmail(payment.getUser().getEmail())
                .orElseThrow(() -> new RuntimeException("Wallet not found."));

        BigDecimal refundedBalance = wallet.getBalance().add(payment.getAmount());
        wallet.setBalance(refundedBalance);

        if (wallet.getTransactions() == null) {
            wallet.setTransactions(new ArrayList<>());
        }

        WalletTransaction walletTransaction = WalletTransaction.builder()
                .wallet(wallet)
                .type("CREDIT")
                .amount(payment.getAmount())
                .balanceAfter(refundedBalance)
                .description("Refund for " + payment.getTransactionCode())
                .payment(payment)
                .build();
        wallet.getTransactions().add(walletTransaction);

        payment.setStatus(PaymentStatus.REFUNDED);
        payment.setRefundedAt(LocalDateTime.now());
        walletRepository.save(wallet);
        paymentRepository.save(payment);
    }
}
