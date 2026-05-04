package com.ytu.gymbackend.controller;

import com.ytu.gymbackend.dto.ApiResponse;
import com.ytu.gymbackend.dto.request.UserRegisterRequest;
import com.ytu.gymbackend.dto.response.UserRegisterResponse;
import com.ytu.gymbackend.dto.response.UserResponse;
import com.ytu.gymbackend.model.user.UserRole;
import com.ytu.gymbackend.service.UserService;
import com.ytu.gymbackend.service.session.UserSessionService;
import com.ytu.gymbackend.validation.ValidPassword;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<UserRegisterResponse> register(
            @Valid @RequestBody UserRegisterRequest request
    ) {
        userSessionService.validatePermission(UserRole.ADMIN);
        UserRegisterResponse response = userService.register(request);
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/password-reset")
    public ResponseEntity<ApiResponse> passwordReset(
            @RequestParam(name = "id") @NotNull Long id,
            @RequestParam(name = "backupSecret") @Valid @NotBlank String backupSecret,
            @RequestParam(name = "newPassword") @Valid @ValidPassword @NotBlank String newPassword
            ) {
        ApiResponse response = userService.passwordReset(id, backupSecret, newPassword);
        return ResponseEntity.status(response.isSuccess() ? 200 : 400).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable @NotNull Long id) {
        userSessionService.validatePermission(UserRole.ADMIN);
        UserResponse response = userService.getUser(id);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        userSessionService.validatePermission(UserRole.ADMIN);
        List<UserResponse> response = userService.getAllUsers();
        return ResponseEntity.status(200).body(response);
    }




}
