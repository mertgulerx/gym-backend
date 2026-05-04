package com.ytu.gymbackend.controller;

import com.ytu.gymbackend.dto.ApiResponse;
import com.ytu.gymbackend.dto.request.ChargeProfileCreateRequest;
import com.ytu.gymbackend.dto.request.MachineCreateRequest;
import com.ytu.gymbackend.dto.response.ChargeProfileResponse;
import com.ytu.gymbackend.dto.response.MachineResponse;
import com.ytu.gymbackend.model.user.UserRole;
import com.ytu.gymbackend.service.SubscriptionService;
import com.ytu.gymbackend.service.session.UserSessionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/subscription")
public class SubscriptionController {
    private final UserSessionService userSessionService;
    private final SubscriptionService subscriptionService;

    public SubscriptionController(UserSessionService userSessionService, SubscriptionService subscriptionService) {
        this.userSessionService = userSessionService;
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/charge-profile/create")
    public ResponseEntity<ChargeProfileResponse> createChargeProfile(
            @Valid @RequestBody ChargeProfileCreateRequest request
    ) {
        userSessionService.validatePermission(UserRole.ADMIN);

        ChargeProfileResponse response = subscriptionService.createChargeProfile(request);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/charge-profile/{id}")
    public ResponseEntity<ChargeProfileResponse> getChargeProfile(
            @PathVariable Long id
    ) {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserRole.ADMIN, UserRole.CLERK)));

        ChargeProfileResponse response = subscriptionService.getChargeProfile(id);
        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/charge-profile/{id}")
    public ResponseEntity<ApiResponse> deleteChargeProfile(
            @PathVariable Long id
    ) {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserRole.ADMIN, UserRole.CLERK)));

        ApiResponse response = subscriptionService.deleteChargeProfile(id);
        return ResponseEntity.status(200).body(response);
    }

    @PutMapping("/charge-profile/{id}")
    public ResponseEntity<ChargeProfileResponse> updateChargeProfile(
            @PathVariable Long id,
            @Valid @RequestBody ChargeProfileCreateRequest request
    ) {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserRole.ADMIN, UserRole.CLERK)));

        ChargeProfileResponse response = subscriptionService.updateChargeProfile(id, request);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/charge-profile")
    public ResponseEntity<List<ChargeProfileResponse>> getAllChargeProfiles() {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserRole.ADMIN, UserRole.CLERK)));

        List<ChargeProfileResponse> response = subscriptionService.getAllChargeProfiles();
        return ResponseEntity.status(200).body(response);
    }
}
