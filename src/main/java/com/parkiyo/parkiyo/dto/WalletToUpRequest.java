package com.parkiyo.parkiyo.dto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public class WalletToUpRequest {

    @Getter @Setter
    public class WalletTopUpRequest {
        @NotNull
        @DecimalMin(value = "0.01", inclusive = true)
        private BigDecimal amount;
    }
}
