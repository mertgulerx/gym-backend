package com.ytu.gymbackend.controller;

import com.ytu.gymbackend.dto.ApiResponse;
import com.ytu.gymbackend.dto.request.UserRegisterRequest;
import com.ytu.gymbackend.model.user.UserType;
import com.ytu.gymbackend.service.UserService;
import com.ytu.gymbackend.service.session.UserSessionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
