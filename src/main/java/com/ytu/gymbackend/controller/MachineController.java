package com.ytu.gymbackend.controller;

import com.ytu.gymbackend.dto.request.MachineCreateRequest;
import com.ytu.gymbackend.dto.response.MachineResponse;
import com.ytu.gymbackend.exception.BadRequestException;
import com.ytu.gymbackend.model.user.UserRole;
import com.ytu.gymbackend.service.MachineService;
import com.ytu.gymbackend.service.session.UserSessionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
    public ResponseEntity<MachineResponse> createMachine(
            @Valid @RequestPart MachineCreateRequest request,
            @RequestPart("file") @NotNull MultipartFile image
    ) {
        if (image.isEmpty() || !((Objects.equals(image.getContentType(), "image/jpeg") || (Objects.equals(image.getContentType(), "image/png"))))) {
            throw new BadRequestException("wrong_file_format");
        }
        userSessionService.validatePermission(UserRole.ADMIN);

        MachineResponse response = machineService.createMachine(request, image);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<ByteArrayResource> getMachineImage(@PathVariable @NotNull Long id) {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserRole.ADMIN, UserRole.REPAIRMAN)));
        byte[] image = machineService.getImage(id);

        ByteArrayResource resource = new ByteArrayResource(image);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + UUID.randomUUID() + ".jpg\"")
                .contentType(MediaType.IMAGE_JPEG)
                .contentLength(image.length)
                .body(resource);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MachineResponse> getMachine(@PathVariable @NotNull Long id) {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserRole.ADMIN, UserRole.REPAIRMAN)));
        MachineResponse response = machineService.getMachine(id);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("")
    public ResponseEntity<List<MachineResponse>> getAllMachines() {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserRole.ADMIN, UserRole.REPAIRMAN)));
        List<MachineResponse> response = machineService.getAllMachines();
        return ResponseEntity.status(200).body(response);
    }
}
