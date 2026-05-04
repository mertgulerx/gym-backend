package com.ytu.gymbackend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Data
public class CustomerRegisterRequest {
    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String surName;

    @NotNull
    @NotBlank
    @Length(min = 11, max = 11)
    @Pattern(regexp = "^[0][0-9]{10}$")
    private String phoneNumber;


}
