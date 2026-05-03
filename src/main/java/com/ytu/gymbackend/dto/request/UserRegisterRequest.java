package com.ytu.gymbackend.dto.request;

import com.ytu.gymbackend.model.user.UserType;
import com.ytu.gymbackend.validation.ValidEnum;
import com.ytu.gymbackend.validation.ValidPassword;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UserRegisterRequest {
    @ValidEnum(enumClass = UserType.class)
    @NotNull
    private String userType;

    @ValidPassword
    @NotNull
    private String password;

    private String backupSecret;
}
