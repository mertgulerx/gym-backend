package com.ytu.gymbackend.dto.request;

import com.ytu.gymbackend.validation.ValidDate;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Data
public class MachineCreateRequest {
    @NotNull
    @NotBlank
    @Length(max = 200)
    private String name;

    @NotNull
    @NotBlank
    @Length(max = 10)
    @ValidDate
    private String lastMaintenanceDate;

    @Min(1)
    @Max(128)
    private Integer maintenanceMonthlyPeriod;
}
