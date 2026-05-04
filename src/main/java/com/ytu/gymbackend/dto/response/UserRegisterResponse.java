package com.ytu.gymbackend.dto.response;

import lombok.Data;
import lombok.Getter;

@Data
public class UserRegisterResponse {
    private Long id;

    private String name;

    private String surName;
}
