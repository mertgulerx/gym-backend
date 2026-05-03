package com.ytu.gymbackend.dto.request;

import lombok.Getter;

@Getter
public class LoginRequest {
    private Long id;
    private String password;
}
