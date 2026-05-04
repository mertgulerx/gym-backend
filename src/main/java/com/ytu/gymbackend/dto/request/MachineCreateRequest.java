package com.ytu.gymbackend.dto.request;

import com.ytu.gymbackend.validation.ValidDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @Size(min = 1, max = 128)
    private Integer maintenanceMonthlyPeriod;
}
