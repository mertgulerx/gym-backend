package com.ytu.gymbackend.dto.request;

import com.ytu.gymbackend.model.user.UserRole;
import com.ytu.gymbackend.validation.ValidEnum;
import com.ytu.gymbackend.validation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UserRegisterRequest {
    @ValidEnum(enumClass = UserRole.class)
    @NotNull
    private String userType;

    @ValidPassword
    @NotNull
    private String password;

    @NotBlank
    private String name;

    @NotBlank
    private String surName;

    @NotBlank
    private String backupSecret;
}
