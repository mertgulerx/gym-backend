package com.ytu.gymbackend.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ChargeProfileResponse {
    private Long id;

    private String title;

    private String info;

    private BigDecimal chargeRate;

    private BigDecimal chargeCost;

    private String creationDate;
}
