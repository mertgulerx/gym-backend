package com.ytu.gymbackend.controller;

import com.ytu.gymbackend.dto.ApiResponse;
import com.ytu.gymbackend.dto.request.MachineCreateRequest;
import com.ytu.gymbackend.model.customer.CustomerHealthReport;
import com.ytu.gymbackend.model.machine.Machine;
import com.ytu.gymbackend.model.user.UserType;
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

import java.net.URLConnection;
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
    public ResponseEntity<ApiResponse> createMachine(
            @Valid @RequestBody MachineCreateRequest request,
            @RequestParam("file") @NotNull MultipartFile image
    ) {
        userSessionService.validatePermission(UserType.ADMIN);

        if (image.isEmpty() || !(Objects.equals(image.getContentType(), "image/jpeg"))) {
            return ResponseEntity.status(400).body(new ApiResponse(false, "wrong_file_format"));
        }

        ApiResponse response = machineService.createMachine(request, image);
        return ResponseEntity.status(response.isSuccess() ? 200 : 400).body(response);
    }

    @GetMapping("/image/get")
    public ResponseEntity<ByteArrayResource> getMachineImage(@RequestParam(name = "id") @NotNull Long id) {
        byte[] image = machineService.getImage(id);

        ByteArrayResource resource = new ByteArrayResource(image);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + UUID.randomUUID() + ".jpg\"")
                .contentType(MediaType.IMAGE_JPEG)
                .contentLength(image.length)
                .body(resource);
    }
}
