package com.ytu.gymbackend.service;

import com.ytu.gymbackend.dto.ApiResponse;
import com.ytu.gymbackend.dto.request.RepairCreateRequest;
import com.ytu.gymbackend.dto.response.RepairResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RepairService {
    RepairResponse createRepair(@NotNull Long machineId, @Valid RepairCreateRequest request);

    ApiResponse completeRepair(Long machineId);

    RepairResponse getLastRepair(Long machineId);

    List<RepairResponse> getAllRepairs(Long machineId);
}
