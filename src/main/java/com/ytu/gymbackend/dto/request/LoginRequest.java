package com.ytu.gymbackend.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Data
public class LoginRequest {
    @NotNull
    @Min(0)
    @Max(1000000)
    private Long id;

    @NotNull
    @Length(min = 8, max = 100)
    @NotBlank
    private String password;
}
