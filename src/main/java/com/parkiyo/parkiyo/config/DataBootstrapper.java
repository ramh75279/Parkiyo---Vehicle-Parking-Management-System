package com.parkiyo.parkiyo.config;

import com.parkiyo.parkiyo.enums.Role;
import com.parkiyo.parkiyo.enums.SlotStatus;
import com.parkiyo.parkiyo.enums.UserStatus;
import com.parkiyo.parkiyo.model.ParkingSlot;
import com.parkiyo.parkiyo.model.User;
import com.parkiyo.parkiyo.model.Wallet;
import com.parkiyo.parkiyo.repository.ParkingSlotRepository;
import com.parkiyo.parkiyo.repository.UserRepository;
import com.parkiyo.parkiyo.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataBootstrapper {

    private final ParkiyoBootstrapProperties properties;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final ParkingSlotRepository slotRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${parkiyo.bootstrap.enabled:true}")
    private boolean bootstrapEnabled;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void onApplicationReady() {
        if (!bootstrapEnabled) {
            log.info("🚫 Bootstrap disabled. Running in clean production mode.");
            return;
        }

        try {
            log.info("🚀 Starting Parkiyo Bootstrap...");

            // Create Admin if not exists
            User admin = userRepository.findByEmail(properties.getAdminEmail())
                    .orElseGet(this::createAdminUser);

            // Ensure wallet exists
            if (walletRepository.findByUserEmail(admin.getEmail()).isEmpty()) {
                Wallet wallet = Wallet.builder().user(admin).build();
                walletRepository.save(wallet);
                log.info("💰 Wallet created for admin");
            }

            // Seed demo slots
            if (properties.isDemoSlotsEnabled() && slotRepository.count() == 0) {
                seedDemoSlots();
            }

            log.info("✅ Bootstrap completed successfully!");

        } catch (Exception e) {
            log.error("❌ Bootstrap failed", e);
        }
    }

    private User createAdminUser() {
        User admin = User.builder()
                .firstName(properties.getAdminFirstName())
                .lastName(properties.getAdminLastName())
                .email(properties.getAdminEmail())
                .password(passwordEncoder.encode(properties.getAdminPassword()))
                .role(Role.ADMIN)
                .status(UserStatus.ACTIVE)
                .emailNotificationsEnabled(true)
                .smsNotificationsEnabled(true)
                .build();

        User saved = userRepository.save(admin);
        log.info("👑 Admin user created: {}", saved.getEmail());
        return saved;
    }

    private void seedDemoSlots() {
        List<ParkingSlot> slots = List.of(
                createSlot("A-001", "Ground Floor", "250.00"),
                createSlot("A-002", "Ground Floor", "250.00"),
                createSlot("A-003", "Ground Floor", "250.00"),
                createSlot("B-001", "Covered", "350.00"),
                createSlot("B-002", "Covered", "350.00"),
                createSlot("EV-001", "EV Charging", "500.00")
        );

        slotRepository.saveAll(slots);
        log.info("🅿️ {} Demo parking slots seeded", slots.size());
    }

    private ParkingSlot createSlot(String slotNumber, String zone, String rate) {
        return ParkingSlot.builder()
                .slotNumber(slotNumber)
                .zone(zone)
                .status(SlotStatus.AVAILABLE)
                .hourlyRate(new BigDecimal(rate))
                .build();
    }
}