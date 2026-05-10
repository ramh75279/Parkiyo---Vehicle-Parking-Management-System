package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.model.Wallet;
import com.parkiyo.parkiyo.model.WalletTransaction;
import com.parkiyo.parkiyo.model.User;
import com.parkiyo.parkiyo.repository.UserRepository;
import com.parkiyo.parkiyo.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public BigDecimal getBalance(String email) {
        Wallet wallet = getOrCreateWallet(email);
        return wallet.getBalance();
    }

    @Transactional
    public void deductBalance(String email, BigDecimal amount, String description) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deduction amount must be positive");
        }

        Wallet wallet = getOrCreateWallet(email);

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient wallet balance.");
        }

        BigDecimal newBalance = wallet.getBalance().subtract(amount);
        wallet.setBalance(newBalance);

        WalletTransaction transaction = WalletTransaction.builder()
                .wallet(wallet)
                .type("DEBIT")
                .amount(amount)
                .balanceAfter(newBalance)
                .description(description)
                .build();

        if (wallet.getTransactions() == null) {
            wallet.setTransactions(new ArrayList<>());
        }
        wallet.getTransactions().add(transaction);

        walletRepository.save(wallet);
    }

    @Transactional
    public void topUp(String email, BigDecimal amount) {
        topUp(email, amount, "Wallet Top-up");
    }

    @Transactional
    public void topUp(String email, BigDecimal amount, String description) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Top-up amount must be positive");
        }

        Wallet wallet = getOrCreateWallet(email);

        BigDecimal newBalance = wallet.getBalance().add(amount);
        wallet.setBalance(newBalance);

        String desc = (description == null || description.isBlank()) ? "Wallet Top-up" : description.trim();

        WalletTransaction transaction = WalletTransaction.builder()
                .wallet(wallet)
                .type("CREDIT")
                .amount(amount)
                .balanceAfter(newBalance)
                .description(desc)
                .build();

        if (wallet.getTransactions() == null) {
            wallet.setTransactions(new ArrayList<>());
        }
        wallet.getTransactions().add(transaction);

        walletRepository.save(wallet);
    }

    @Transactional(readOnly = true)
    public List<WalletTransaction> getTransactionHistory(String email) {
        Wallet wallet = getOrCreateWallet(email);
        List<WalletTransaction> transactions = wallet.getTransactions();
        return transactions != null ? new ArrayList<>(transactions) : new ArrayList<>();
    }

    /**
     * Returns wallet overview data for the controller.
     *
     * FIX: Instead of calling wallet.getUser() (which returns a Hibernate lazy proxy
     * that becomes inaccessible after the session closes), we load the User directly
     * from UserRepository using the email we already have. This prevents the
     * LazyInitializationException that occurs when Thymeleaf accesses user.initials
     * or other properties after the transaction ends.
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getWalletOverview(String email) {
        Wallet wallet = getOrCreateWallet(email);

        // Load User directly — do NOT use wallet.getUser() here because that
        // relationship is LAZY and the proxy cannot be initialized outside a session.
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        List<WalletTransaction> transactions = wallet.getTransactions();

        Map<String, Object> overview = new HashMap<>();
        overview.put("wallet", wallet);
        overview.put("user", user);
        overview.put("balance", wallet.getBalance());
        overview.put("transactions", transactions != null ? new ArrayList<>(transactions) : new ArrayList<>());

        return overview;
    }

    private Wallet getOrCreateWallet(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        return walletRepository.findByUser_Email(email)
                .orElseGet(() -> {
                    User user = userRepository.findByEmail(email)
                            .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

                    Wallet wallet = Wallet.builder()
                            .user(user)
                            .balance(BigDecimal.ZERO)
                            .transactions(new ArrayList<>())
                            .build();

                    return walletRepository.save(wallet);
                });
    }
}