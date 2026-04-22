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
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(ParkiyoBootstrapProperties.class)
public class DataBootstrapper implements CommandLineRunner {

    private final ParkiyoBootstrapProperties properties;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final ParkingSlotRepository slotRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (!properties.isEnabled()) {
            return;
        }

        User admin = userRepository.findByEmail(properties.getAdminEmail())
                .orElseGet(this::createAdminUser);

        walletRepository.findByUserEmail(admin.getEmail())
                .orElseGet(() -> walletRepository.save(Wallet.builder().user(admin).build()));

        if (properties.isDemoSlotsEnabled() && slotRepository.count() == 0) {
            seedSlots();
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
        return userRepository.save(admin);
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
    }

    private ParkingSlot slot(String slotNumber, String zone, String hourlyRate) {
        return ParkingSlot.builder()
                .slotNumber(slotNumber)
                .zone(zone)
                .status(SlotStatus.AVAILABLE)
                .hourlyRate(new BigDecimal(hourlyRate))
                .build();
    }
}
