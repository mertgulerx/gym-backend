package com.ytu.gymbackend.dto.response;

import lombok.Data;

@Data
public class CustomerRegisterResponse {
    private Long id;

    private String name;

    private String surName;

    private String phoneNumber;
}
