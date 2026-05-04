package com.ytu.gymbackend.controller;

import com.ytu.gymbackend.dto.request.MaintenanceCreateRequest;
import com.ytu.gymbackend.dto.response.MaintenanceResponse;
import com.ytu.gymbackend.model.user.UserRole;
import com.ytu.gymbackend.service.MaintenanceService;
import com.ytu.gymbackend.service.session.UserSessionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/machine")
public class MaintenanceController {
    private final UserSessionService userSessionService;
    private final MaintenanceService maintenanceService;

    public MaintenanceController(UserSessionService userSessionService, MaintenanceService maintenanceService) {
        this.userSessionService = userSessionService;
        this.maintenanceService = maintenanceService;
    }

    @PostMapping("/{machineId}/maintenance")
    public ResponseEntity<MaintenanceResponse> createMaintenance(
            @PathVariable @NotNull Long machineId,
            @Valid @RequestBody MaintenanceCreateRequest request
    ) {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserRole.ADMIN, UserRole.REPAIRMAN)));

        MaintenanceResponse response = maintenanceService.createMaintenance(machineId, request);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/{machineId}/maintenance/last")
    public ResponseEntity<MaintenanceResponse> getLastMaintenance(
            @PathVariable @NotNull Long machineId
    ) {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserRole.ADMIN, UserRole.REPAIRMAN)));

        MaintenanceResponse response = maintenanceService.getLastMaintenance(machineId);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/{machineId}/maintenance/all")
    public ResponseEntity<List<MaintenanceResponse>> getAllMaintenances(
            @PathVariable @NotNull Long machineId
    ) {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserRole.ADMIN, UserRole.REPAIRMAN)));

        List<MaintenanceResponse> response = maintenanceService.getAllMaintenances(machineId);
        return ResponseEntity.status(200).body(response);
    }

}
