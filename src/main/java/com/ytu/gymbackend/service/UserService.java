package com.ytu.gymbackend.service;

import com.ytu.gymbackend.dto.ApiResponse;
import com.ytu.gymbackend.dto.request.LoginRequest;
import com.ytu.gymbackend.dto.request.UserRegisterRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    String login(@Valid LoginRequest request);

    ApiResponse register(@Valid UserRegisterRequest request);

    ApiResponse passwordReset(Long id, String backupSecret, String newPassword);
}
