package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.enums.PaymentStatus;
import com.parkiyo.parkiyo.model.ParkingRecord;
import com.parkiyo.parkiyo.model.Payment;
import com.parkiyo.parkiyo.model.Receipt;
import com.parkiyo.parkiyo.model.Reservation;
import com.parkiyo.parkiyo.model.Wallet;
import com.parkiyo.parkiyo.model.WalletTransaction;
import com.parkiyo.parkiyo.repository.PaymentRepository;
import com.parkiyo.parkiyo.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<Receipt> getUserReceipts(String email) {
        return paymentRepository.findByUserEmailAndStatus(email, PaymentStatus.SUCCESS).stream()
                .map(Payment::getReceipt)
                .filter(r -> r != null)
                .collect(Collectors.toList());
    }

    public Receipt getReceipt(Long paymentId, String email) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Receipt not found."));

        if (!payment.getUser().getEmail().equalsIgnoreCase(email)) {
            throw new RuntimeException("You are not allowed to view this receipt.");
        }

        if (payment.getReceipt() == null) {
            throw new RuntimeException("Receipt not found.");
        }
        return payment.getReceipt();
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

        // Save a receipt snapshot so the UI can render without deep lazy-loading chains.
        Receipt receipt = buildReceiptSnapshot(payment);
        payment.setReceipt(receipt);
        walletRepository.save(wallet);
        paymentRepository.save(payment);
    }

    private Receipt buildReceiptSnapshot(Payment payment) {
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
                if (make != null && model != null) {
                    vehicleModel = make + " " + model;
                } else if (model != null) {
                    vehicleModel = model;
                } else {
                    vehicleModel = make;
                }
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
                if (make != null && model != null) {
                    vehicleModel = make + " " + model;
                } else if (model != null) {
                    vehicleModel = model;
                } else if (vehicleModel == null) {
                    vehicleModel = make;
                }
            }

            if (slotCode == null && reservation.getSlot() != null) {
                slotCode = reservation.getSlot().getSlotNumber();
                zone = reservation.getSlot().getZone();
            }

            if (arrival == null) {
                arrival = reservation.getStartTime();
            }
            if (departure == null) {
                departure = reservation.getEndTime();
            }
        }

        if (durationMinutes == null && arrival != null && departure != null && !departure.isBefore(arrival)) {
            durationMinutes = (int) Duration.between(arrival, departure).toMinutes();
        }

        String durationText = "-";
        if (durationMinutes != null && durationMinutes > 0) {
            long hours = durationMinutes / 60;
            long minutes = durationMinutes % 60;
            durationText = minutes == 0 ? hours + "h" : hours + "h " + minutes + "m";
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
