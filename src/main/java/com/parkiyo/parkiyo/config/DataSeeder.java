package com.parkiyo.parkiyo.config;

import com.parkiyo.parkiyo.enums.Role;
import com.parkiyo.parkiyo.enums.SlotStatus;
import com.parkiyo.parkiyo.enums.UserStatus;
import com.parkiyo.parkiyo.enums.VehicleCategory;
import com.parkiyo.parkiyo.model.*;
import com.parkiyo.parkiyo.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("!prod")
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final ParkingSlotRepository slotRepository;
    private final ParkingRecordRepository parkingRecordRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 10) {
            log.info("✅ Sufficient demo data already exists. Skipping seeder.");
            return;
        }

        log.info("🌱 Seeding realistic demo data...");

        // Create sample users if not enough exist
        createUsersIfNeeded();

        // Create vehicles
        createVehiclesIfNeeded();

        // Create slots (safely)
        createSlotsSafely();

        log.info("✅ Demo data seeding completed!");
    }

    private void createUsersIfNeeded() {
        if (userRepository.findByEmail("kamal@parkiyo.com").isEmpty()) {
            User user1 = User.builder()
                    .firstName("Kamal")
                    .lastName("Perera")
                    .email("kamal@parkiyo.com")
                    .password("$2a$10$dummyhashedpassword")
                    .role(Role.USER)
                    .status(UserStatus.ACTIVE)
                    .build();
            userRepository.save(user1);
        }

        if (userRepository.findByEmail("nimali@parkiyo.com").isEmpty()) {
            User user2 = User.builder()
                    .firstName("Nimali")
                    .lastName("Silva")
                    .email("nimali@parkiyo.com")
                    .password("$2a$10$dummyhashedpassword")
                    .role(Role.USER)
                    .status(UserStatus.ACTIVE)
                    .build();
            userRepository.save(user2);
        }
    }

    private void createVehiclesIfNeeded() {
        if (vehicleRepository.count() < 3) {
            User kamal = userRepository.findByEmail("kamal@parkiyo.com").orElse(null);
            User nimali = userRepository.findByEmail("nimali@parkiyo.com").orElse(null);

            vehicleRepository.save(Vehicle.builder()
                    .licensePlate("ABC-1234").category(VehicleCategory.CAR)
                    .make("Toyota").model("Prius").color("Silver").year(2023)
                    .user(kamal).active(true).build());

            vehicleRepository.save(Vehicle.builder()
                    .licensePlate("DEF-5678").category(VehicleCategory.CAR)
                    .make("Honda").model("Civic").color("Blue").year(2022)
                    .user(kamal).active(true).build());

            vehicleRepository.save(Vehicle.builder()
                    .licensePlate("GHI-9012").category(VehicleCategory.MOTORCYCLE)
                    .make("Yamaha").model("R15").color("Black").year(2024)
                    .user(nimali).active(true).build());
        }
    }

    private void createSlotsSafely() {
        if (slotRepository.count() >= 5) return;

        List<ParkingSlot> slots = List.of(
                createSlotIfNotExists("A-101", "Ground Floor", SlotStatus.OCCUPIED),
                createSlotIfNotExists("A-102", "Ground Floor", SlotStatus.AVAILABLE),
                createSlotIfNotExists("B-201", "First Floor", SlotStatus.OCCUPIED),
                createSlotIfNotExists("B-202", "First Floor", SlotStatus.AVAILABLE),
                createSlotIfNotExists("EV-001", "EV Zone", SlotStatus.AVAILABLE)
        );

        slotRepository.saveAll(slots.stream().filter(s -> s != null).toList());
    }

    private ParkingSlot createSlotIfNotExists(String number, String zone, SlotStatus status) {
        if (slotRepository.existsBySlotNumber(number)) return null;
        return ParkingSlot.builder()
                .slotNumber(number)
                .zone(zone)
                .status(status)
                .hourlyRate(new BigDecimal("250.00"))
                .build();
    }
}