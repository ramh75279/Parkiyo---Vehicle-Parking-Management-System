package com.parkiyo.parkiyo;

import com.parkiyo.parkiyo.repository.ParkingSlotRepository;
import com.parkiyo.parkiyo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ParkiyoApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ParkingSlotRepository parkingSlotRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoadsAndBootstrapsOperationalData() {
        assertThat(userRepository.findByEmail("admin@parkiyo.com")).isPresent();
        assertThat(parkingSlotRepository.count()).isGreaterThanOrEqualTo(6);
    }

    @Test
    void publicPasswordResetPageRenders() throws Exception {
        mockMvc.perform(get("/reset-password").param("token", "sample-token"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/reset-password"));
    }

    @Test
    @WithMockUser(username = "admin@parkiyo.com", roles = "ADMIN")
    void logoutPageClearsSessionAndRenders() throws Exception {
        mockMvc.perform(get("/logout"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/logout"));
    }

    @Test
    @WithMockUser(username = "admin@parkiyo.com", roles = "ADMIN")
    void adminCompatibilityRoutesResolve() throws Exception {
        mockMvc.perform(get("/admin/audit-log"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/audit"));

        mockMvc.perform(get("/admin/slots/edit/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/slots/1/edit"));

        mockMvc.perform(get("/admin/slots/history/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/slots/1/usage-history"));

        mockMvc.perform(get("/admin/receipts/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/payments"));

        mockMvc.perform(get("/admin/vehicles/by-category"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/vehicles/browse"));
    }

    @Test
    @WithMockUser(username = "user@parkiyo.com", roles = "USER")
    void userCompatibilityRoutesResolve() throws Exception {
        mockMvc.perform(get("/payments"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payments/history"));

        mockMvc.perform(get("/parking/record-details"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/parking"));

        mockMvc.perform(get("/slots/select"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reservations/slot-selection"));
    }
}
