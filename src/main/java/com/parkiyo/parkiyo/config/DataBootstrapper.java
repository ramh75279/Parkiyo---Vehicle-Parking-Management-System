package com.parkiyo.parkiyo.config;

import com.parkiyo.parkiyo.enums.PassPlanCategory;
import com.parkiyo.parkiyo.enums.Role;
import com.parkiyo.parkiyo.enums.SlotStatus;
import com.parkiyo.parkiyo.enums.UserStatus;
import com.parkiyo.parkiyo.model.ParkingSlot;
import com.parkiyo.parkiyo.model.PassPlan;
import com.parkiyo.parkiyo.model.User;
import com.parkiyo.parkiyo.model.Wallet;
import com.parkiyo.parkiyo.repository.ParkingSlotRepository;
import com.parkiyo.parkiyo.repository.PassPlanRepository;
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
    private final PassPlanRepository passPlanRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${parkiyo.bootstrap.enabled:true}")
    private boolean bootstrapEnabled;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void onApplicationReady() {
        if (!bootstrapEnabled) {
            log.info("🚫 Bootstrap disabled.");
            return;
        }

        log.info("🚀 Parkiyo Bootstrap Starting...");
        createAdminIfNotExists();

        if (properties.isDemoSlotsEnabled() && slotRepository.count() == 0) {
            seedDemoSlots();
        }

        seedPassPlansIfEmpty();

        log.info("✅ Bootstrap completed successfully!");
    }

    private void createAdminIfNotExists() {
        if (userRepository.findByEmail(properties.getAdminEmail()).isPresent()) {
            log.info("👑 Admin already exists.");
            return;
        }

        User admin = User.builder()
                .firstName(properties.getAdminFirstName())
                .lastName(properties.getAdminLastName())
                .email(properties.getAdminEmail())
                .password(passwordEncoder.encode(properties.getAdminPassword()))
                .role(Role.ADMIN)
                .status(UserStatus.ACTIVE)
                .emailNotificationsEnabled(true)
                .smsNotificationsEnabled(true)
                .emailVerified(true)
                .build();

        userRepository.save(admin);
        log.info("✅ Admin user created: {}", admin.getEmail());

        Wallet wallet = Wallet.builder().user(admin).balance(BigDecimal.valueOf(5000)).build();
        walletRepository.save(wallet);
        log.info("💰 Admin wallet created with balance 5000");
    }

    private void seedDemoSlots() {
        List<ParkingSlot> slots = List.of(
                createSlot("A-001", "Ground Floor", "250.00"),
                createSlot("A-002", "Ground Floor", "250.00"),
                createSlot("A-003", "Ground Floor", "250.00"),
                createSlot("B-001", "First Floor", "300.00"),
                createSlot("B-002", "First Floor", "300.00"),
                createSlot("EV-001", "EV Section", "450.00")
        );

        slotRepository.saveAll(slots);
        log.info("🅿️ {} Demo parking slots created", slots.size());
    }

    private ParkingSlot createSlot(String slotNumber, String zone, String rate) {
        return ParkingSlot.builder()
                .slotNumber(slotNumber)
                .zone(zone)
                .status(SlotStatus.AVAILABLE)
                .hourlyRate(new BigDecimal(rate))
                .build();
    }

    private void seedPassPlansIfEmpty() {
        if (passPlanRepository.count() > 0) {
            return;
        }
        List<PassPlan> plans = List.of(
                PassPlan.builder()
                        .name("Weekly Flex")
                        .description("50% off exit fees for 7 days — any zone.")
                        .category(PassPlanCategory.WEEKLY)
                        .price(BigDecimal.valueOf(1500))
                        .durationDays(7)
                        .zone(null)
                        .unlimitedParking(false)
                        .discountPercent(BigDecimal.valueOf(50))
                        .active(true)
                        .displayOrder(10)
                        .build(),
                PassPlan.builder()
                        .name("Monthly Value")
                        .description("40% off for 30 days — ideal for daily commuters.")
                        .category(PassPlanCategory.MONTHLY)
                        .price(BigDecimal.valueOf(5000))
                        .durationDays(30)
                        .zone(null)
                        .unlimitedParking(false)
                        .discountPercent(BigDecimal.valueOf(40))
                        .active(true)
                        .displayOrder(20)
                        .build(),
                PassPlan.builder()
                        .name("VIP Unlimited")
                        .description("No exit parking charges for 14 days system-wide.")
                        .category(PassPlanCategory.VIP)
                        .price(BigDecimal.valueOf(12000))
                        .durationDays(14)
                        .zone(null)
                        .unlimitedParking(true)
                        .discountPercent(BigDecimal.ZERO)
                        .active(true)
                        .displayOrder(30)
                        .build(),
                PassPlan.builder()
                        .name("Employee Ground Floor")
                        .description("Unlimited parking when using Ground Floor slots.")
                        .category(PassPlanCategory.EMPLOYEE)
                        .price(BigDecimal.valueOf(2500))
                        .durationDays(30)
                        .zone("Ground Floor")
                        .unlimitedParking(true)
                        .discountPercent(BigDecimal.ZERO)
                        .active(true)
                        .displayOrder(40)
                        .build()
        );
        passPlanRepository.saveAll(plans);
        log.info("🎫 {} default parking pass plans created", plans.size());
    }
}