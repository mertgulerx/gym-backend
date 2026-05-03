package com.ytu.gymbackend.controller;

import com.ytu.gymbackend.dto.ApiResponse;
import com.ytu.gymbackend.dto.request.MachineCreateRequest;
import com.ytu.gymbackend.model.user.UserType;
import com.ytu.gymbackend.service.MachineService;
import com.ytu.gymbackend.service.session.UserSessionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/machine")
public class MachineController {
    private final UserSessionService userSessionService;
    private final MachineService machineService;

    public MachineController(UserSessionService userSessionService, MachineService machineService) {
        this.userSessionService = userSessionService;
        this.machineService = machineService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createMachine(
            @Valid @RequestBody MachineCreateRequest request
    ) {
        userSessionService.validatePermission(UserType.ADMIN);
        ApiResponse response = machineService.createMachine(request);
        return ResponseEntity.status(response.isSuccess() ? 200 : 400).body(response);
    }
}
