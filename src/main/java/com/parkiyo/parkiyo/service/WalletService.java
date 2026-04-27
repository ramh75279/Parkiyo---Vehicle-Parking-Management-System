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
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    public BigDecimal getBalance(String email) {
        return getOrCreateWallet(email).getBalance();
    }

    @Transactional
    public Map<String, Object> getWalletOverview(String email) {
        Wallet wallet = getOrCreateWallet(email);
        return Map.of(
                "balance", wallet.getBalance(),
                "wallet", wallet
        );
    }

    @Transactional
    public List<WalletTransaction> getTransactionHistory(String email) {
        Wallet wallet = getOrCreateWallet(email);
        List<WalletTransaction> transactions = wallet.getTransactions();
        transactions.size(); // force-loads the lazy collection
        return transactions;


    }

    @Transactional
    public void topUp(String email, BigDecimal amount) {
        Wallet wallet = getOrCreateWallet(email);

        BigDecimal newBalance = wallet.getBalance().add(amount);
        wallet.setBalance(newBalance);

        if (wallet.getTransactions() == null) {
            wallet.setTransactions(new java.util.ArrayList<>());
        }

        WalletTransaction transaction = WalletTransaction.builder()
                .wallet(wallet)
                .type("CREDIT")
                .amount(amount)
                .balanceAfter(newBalance)
                .description("Top-up")
                .build();
        wallet.getTransactions().add(transaction);
        walletRepository.save(wallet);
    }

    private Wallet getOrCreateWallet(String email) {
        return walletRepository.findByUserEmail(email)
                .orElseGet(() -> {
                    User user = userRepository.findByEmail(email)
                            .orElseThrow(() -> new RuntimeException("User not found: " + email));

                    Wallet wallet = Wallet.builder()
                            .user(user)
                            .balance(BigDecimal.ZERO)
                            .transactions(new ArrayList<>())
                            .build();

                    return walletRepository.save(wallet);
                });
    }
}
