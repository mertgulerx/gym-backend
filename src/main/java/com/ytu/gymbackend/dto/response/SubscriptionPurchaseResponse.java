package com.ytu.gymbackend.dto.response;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SubscriptionPurchaseResponse {
    private Long id;

    private Long subscriptionId;

    private String creationDate;

    private Boolean isCompleted;

    private String title;

    private Integer subscriptionDays;

    private Integer subscriptionMonthPeriod;

    private BigDecimal monthlyCost;

    private BigDecimal totalCost;

    private Boolean isTimeLimited;

    @Nullable
    private Integer startHour;

    @Nullable
    private Integer endHour;
}
