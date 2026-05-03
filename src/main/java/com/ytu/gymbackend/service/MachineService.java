package com.ytu.gymbackend.service;

import com.ytu.gymbackend.dto.ApiResponse;
import com.ytu.gymbackend.dto.request.MachineCreateRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public interface MachineService {
    ApiResponse createMachine(@Valid MachineCreateRequest request);
}
