package com.parkiyo.parkiyo;

import com.parkiyo.parkiyo.repository.ParkingSlotRepository;
import com.parkiyo.parkiyo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class ParkiyoApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ParkingSlotRepository parkingSlotRepository;

    @Test
    void contextLoadsAndBootstrapsOperationalData() {
        assertThat(userRepository.findByEmail("admin@parkiyo.com")).isPresent();
        assertThat(parkingSlotRepository.count()).isGreaterThanOrEqualTo(6);
    }
}
