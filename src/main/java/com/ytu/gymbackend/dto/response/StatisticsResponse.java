package com.ytu.gymbackend.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StatisticsResponse {
    private String startDate;

    private String endDate;

    private BigDecimal totalRevenue;

    private BigDecimal maintenanceCosts;

    private BigDecimal repairCosts;
}
