package com.ytu.gymbackend.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RepairResponse {
    private Long id;

    private Long machineId;

    private String sentDate;

    private String completeDay;

    private Long maintainerId;

    private BigDecimal cost;

    private String info;

    private Integer estimatedReturnDays;

    private Boolean isCompleted;
}
