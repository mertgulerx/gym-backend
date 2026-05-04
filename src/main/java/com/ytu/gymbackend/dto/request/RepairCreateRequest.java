package com.ytu.gymbackend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RepairCreateRequest {
    @NotNull
    @Min(0)
    private BigDecimal cost;

    @NotBlank
    private String info;

    @NotNull
    private Integer estimatedReturnDays;

    @NotNull
    private Boolean isCompleted;
}
