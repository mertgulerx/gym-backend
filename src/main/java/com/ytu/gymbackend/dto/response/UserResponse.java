package com.ytu.gymbackend.dto.response;

import com.ytu.gymbackend.model.user.UserRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {
    private Long id;

    private String name;

    private String surName;

    private String accountCreationDate;

    private String userRole;
}
