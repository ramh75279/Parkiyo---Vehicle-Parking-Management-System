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
import org.springframework.boot.context.properties.EnableConfigurationProperties;
<<<<<<< HEAD
import org.springframework.context.event.EventListener;
=======
import org.springframework.jdbc.core.JdbcTemplate;
>>>>>>> ad05a9c0b3e692785b11ded84200b636e322d444
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(ParkiyoBootstrapProperties.class)
@Slf4j
public class DataBootstrapper {

    private final ParkiyoBootstrapProperties properties;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final ParkingSlotRepository slotRepository;
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    @Value("${parkiyo.bootstrap.enabled:false}")
    private boolean bootstrapEnabled;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void onApplicationReady() {
        // Respect both property sources for flexibility
        if (!bootstrapEnabled || !properties.isEnabled()) {
            log.info("🚫 Bootstrap disabled - Running in REAL production mode");
            return;
        }

<<<<<<< HEAD
        log.info("🌱 Bootstrap enabled - Seeding initial data...");
=======
        ensureUsersWalletBalanceHasDefault();

        User admin = userRepository.findByEmail(properties.getAdminEmail())
                .orElseGet(this::createAdminUser);
>>>>>>> ad05a9c0b3e692785b11ded84200b636e322d444

        try {
            // Create Admin User if not exists
            User admin = userRepository.findByEmail(properties.getAdminEmail())
                    .orElseGet(this::createAdminUser);

            // Create Wallet for Admin
            walletRepository.findByUserEmail(admin.getEmail())
                    .orElseGet(() -> walletRepository.save(Wallet.builder().user(admin).build()));

            // Seed Demo Slots (only if enabled and no slots exist)
            if (properties.isDemoSlotsEnabled() && slotRepository.count() == 0) {
                seedSlots();
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
        log.info("👤 Admin user created: {}", saved.getEmail());
        return saved;
    }

    private void seedSlots() {
        List<ParkingSlot> slots = List.of(
                slot("A-001", "Ground Floor", "250.00"),
                slot("A-002", "Ground Floor", "250.00"),
                slot("A-003", "Ground Floor", "250.00"),
                slot("B-001", "Covered", "350.00"),
                slot("B-002", "Covered", "350.00"),
                slot("EV-001", "EV Charging", "500.00")
        );
        slotRepository.saveAll(slots);
        log.info("🅿️ {} Demo parking slots seeded", slots.size());
    }

    private ParkingSlot slot(String slotNumber, String zone, String hourlyRate) {
        return ParkingSlot.builder()
                .slotNumber(slotNumber)
                .zone(zone)
                .status(SlotStatus.AVAILABLE)
                .hourlyRate(new BigDecimal(hourlyRate))
                .build();
    }
<<<<<<< HEAD
}
=======

    private void ensureUsersWalletBalanceHasDefault() {
        Integer hasWalletBalanceColumn = jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM information_schema.columns
                WHERE table_schema = DATABASE()
                  AND table_name = 'users'
                  AND column_name = 'wallet_balance'
                """,
                Integer.class
        );

        if (hasWalletBalanceColumn != null && hasWalletBalanceColumn > 0) {
            jdbcTemplate.execute("""
                    ALTER TABLE users
                    MODIFY COLUMN wallet_balance DECIMAL(10,2) NOT NULL DEFAULT 0.00
                    """);
        }
    }
}
>>>>>>> ad05a9c0b3e692785b11ded84200b636e322d444
