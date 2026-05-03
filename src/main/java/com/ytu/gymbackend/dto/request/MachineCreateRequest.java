package com.ytu.gymbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class MachineCreateRequest {
    @NotNull
    @NotBlank
    @Length(max = 200)
    private String name;

    @NotNull
    @NotBlank
    @Length(max = 10)
    private String lastMaintenanceDate;
}
