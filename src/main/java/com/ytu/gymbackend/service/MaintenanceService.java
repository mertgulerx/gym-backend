package com.ytu.gymbackend.service;

import com.ytu.gymbackend.dto.request.MaintenanceCreateRequest;
import com.ytu.gymbackend.dto.response.MachineResponse;
import com.ytu.gymbackend.dto.response.MaintenanceResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MaintenanceService {
    MaintenanceResponse createMaintenance(@NotNull Long machineId, @Valid MaintenanceCreateRequest request);

    MaintenanceResponse getLastMaintenance(Long machineId);

    List<MaintenanceResponse> getAllMaintenances(Long machineId);
}
