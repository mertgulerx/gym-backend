package com.ytu.gymbackend.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MaintenanceResponse {
    private Long id;

    private Long machineId;

    private String creationDate;

    private Long maintainerId;

    private BigDecimal cost;

    private String info;
}
