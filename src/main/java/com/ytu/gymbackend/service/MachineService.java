package com.ytu.gymbackend.service;

import com.ytu.gymbackend.dto.ApiResponse;
import com.ytu.gymbackend.dto.request.MachineCreateRequest;
import com.ytu.gymbackend.model.machine.Machine;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface MachineService {
    ApiResponse createMachine(@Valid MachineCreateRequest request, @NotNull MultipartFile image);

    byte[] getImage(@NotNull Long id);
}
