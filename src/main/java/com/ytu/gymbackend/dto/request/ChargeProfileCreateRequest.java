package com.ytu.gymbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ChargeProfileCreateRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String info;

    @NotNull
    private BigDecimal chargeRate;

    @NotNull
    private BigDecimal chargeCost;
}
