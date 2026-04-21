package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.model.Wallet;
import com.parkiyo.parkiyo.model.WalletTransaction;
import com.parkiyo.parkiyo.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    public BigDecimal getBalance(String email) {
        return walletRepository.findByUserEmail(email)
                .map(Wallet::getBalance)
                .orElse(BigDecimal.ZERO);
    }

    public Map<String, Object> getWalletOverview(String email) {
        Wallet wallet = walletRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Wallet not found."));
        return Map.of(
                "balance", wallet.getBalance(),
                "wallet", wallet
        );
    }

    public List<WalletTransaction> getTransactionHistory(String email) {
        Wallet wallet = walletRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Wallet not found."));
        return wallet.getTransactions();
    }

    @Transactional
    public void topUp(String email, BigDecimal amount) {
        Wallet wallet = walletRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Wallet not found."));

        BigDecimal newBalance = wallet.getBalance().add(amount);
        wallet.setBalance(newBalance);

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
}
