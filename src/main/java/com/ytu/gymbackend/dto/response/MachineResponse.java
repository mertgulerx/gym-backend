package com.ytu.gymbackend.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MachineResponse {
    private Long id;

    private String name;

    private LocalDate lastMaintenanceDate;

    private Integer maintenanceMonthlyPeriod;

    private String machineStatus;
}
