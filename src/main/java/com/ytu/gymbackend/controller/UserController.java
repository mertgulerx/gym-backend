package com.ytu.gymbackend.controller;

import com.ytu.gymbackend.dto.ApiResponse;
import com.ytu.gymbackend.dto.request.LoginRequest;
import com.ytu.gymbackend.dto.request.UserRegisterRequest;
import com.ytu.gymbackend.model.user.UserType;
import com.ytu.gymbackend.service.UserService;
import com.ytu.gymbackend.service.session.UserSessionService;
import com.ytu.gymbackend.validation.ValidPassword;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final UserSessionService userSessionService;

    public UserController(UserService userService, UserSessionService userSessionService) {
        this.userService = userService;
        this.userSessionService = userSessionService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(
            @Valid @RequestBody UserRegisterRequest request
    ) {
        userSessionService.validatePermission(UserType.ADMIN);
        ApiResponse response = userService.register(request);
        return ResponseEntity.status(response.isSuccess() ? 200 : 400).body(response);
    }

    @PostMapping("/password-reset")
    public ResponseEntity<ApiResponse> passwordReset(
            @RequestParam(name = "id") Long id,
            @RequestParam(name = "backupSecret") @Valid @NotBlank String backupSecret,
            @RequestParam(name = "newPassword") @Valid @ValidPassword @NotBlank String newPassword
            ) {
        ApiResponse response = userService.passwordReset(id, backupSecret, newPassword);
        return ResponseEntity.status(response.isSuccess() ? 200 : 400).body(response);
    }


}
