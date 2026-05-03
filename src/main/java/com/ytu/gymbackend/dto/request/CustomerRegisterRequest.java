package com.ytu.gymbackend.dto.request;

import com.ytu.gymbackend.validation.ValidTcKimlikNo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class CustomerRegisterRequest {
    @NotBlank()
    @NotNull
    @ValidTcKimlikNo
    private String tcKimlikNo;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String surName;

    @NotBlank(message = "Email cannot be blank")
    @Length(max = 128, message = "Email is too long")
    @Email(message = "Enter a valid email address", regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    @NotNull
    private String email;

    @NotNull
    @NotBlank
    @Length(min = 11, max = 11)
    @Pattern(regexp = "^[0][0-9]{10}$")
    private String phoneNumber;


}
