package com.ytu.gymbackend.dto.request;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SubscriptionPurchaseRequest {
    @NotBlank
    private String title;

    @Max(30)
    @Min(8)
    @NotNull
    private Integer subscriptionDays;

    @Min(1)
    @NotNull
    private Integer subscriptionMonthPeriod;

    @NotNull
    private BigDecimal chargeRate;

    @NotNull
    private BigDecimal chargeCost;

    @NotNull
    private Boolean isTimeLimited;

    @Nullable
    private Integer startHour;

    @Nullable
    private Integer endHour;
}
