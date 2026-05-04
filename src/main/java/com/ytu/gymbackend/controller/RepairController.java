package com.ytu.gymbackend.controller;

import com.ytu.gymbackend.dto.request.MaintenanceCreateRequest;
import com.ytu.gymbackend.dto.request.RepairCreateRequest;
import com.ytu.gymbackend.dto.response.MaintenanceResponse;
import com.ytu.gymbackend.dto.response.RepairResponse;
import com.ytu.gymbackend.model.user.UserRole;
import com.ytu.gymbackend.service.MaintenanceService;
import com.ytu.gymbackend.service.RepairService;
import com.ytu.gymbackend.service.session.UserSessionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/machine")
public class RepairController {
    private final UserSessionService userSessionService;
    private final MaintenanceService maintenanceService;
    private final RepairService repairService;

    public RepairController(UserSessionService userSessionService, MaintenanceService maintenanceService, RepairService repairService) {
        this.userSessionService = userSessionService;
        this.maintenanceService = maintenanceService;
        this.repairService = repairService;
    }

    @PostMapping("/{machineId}/repair")
    public ResponseEntity<RepairResponse> createMaintenance(
            @PathVariable @NotNull Long machineId,
            @Valid @RequestBody RepairCreateRequest request
    ) {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserRole.ADMIN, UserRole.REPAIRMAN)));

        RepairResponse response = repairService.createRepair(machineId, request);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/{machineId}/repair/last")
    public ResponseEntity<RepairResponse> getLastMaintenance(
            @PathVariable @NotNull Long machineId
    ) {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserRole.ADMIN, UserRole.REPAIRMAN)));

        RepairResponse response = repairService.getLastRepair(machineId);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/{machineId}/repair/all")
    public ResponseEntity<List<RepairResponse>> getAllMaintenances(
            @PathVariable @NotNull Long machineId
    ) {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserRole.ADMIN, UserRole.REPAIRMAN)));

        List<RepairResponse> response = repairService.getAllRepairs(machineId);
        return ResponseEntity.status(200).body(response);
    }

}
