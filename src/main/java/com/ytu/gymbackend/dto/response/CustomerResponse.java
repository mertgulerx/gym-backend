package com.ytu.gymbackend.dto.response;

import lombok.Data;

@Data
public class CustomerResponse {
    private Long id;

    private String name;

    private String surName;

    private String phoneNumber;

    private String customerStatus;

    private String accountCreationDate;

    private Boolean isActiveSubscriber;
}
