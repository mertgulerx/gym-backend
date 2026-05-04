package com.ytu.gymbackend.service;

import com.ytu.gymbackend.dto.ApiResponse;
import com.ytu.gymbackend.dto.request.LoginRequest;
import com.ytu.gymbackend.dto.request.UserRegisterRequest;
import com.ytu.gymbackend.dto.response.UserRegisterResponse;
import com.ytu.gymbackend.dto.response.UserResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    String login(@Valid LoginRequest request);

    UserRegisterResponse register(@Valid UserRegisterRequest request);

    ApiResponse passwordReset(Long id, String backupSecret, String newPassword);

    UserResponse getUser(@NotNull Long id);

    List<UserResponse> getAllUsers();
}
