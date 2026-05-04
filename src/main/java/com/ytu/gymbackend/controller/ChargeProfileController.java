package com.ytu.gymbackend.controller;

import com.ytu.gymbackend.dto.ApiResponse;
import com.ytu.gymbackend.dto.request.ChargeProfileCreateRequest;
import com.ytu.gymbackend.dto.response.ChargeProfileResponse;
import com.ytu.gymbackend.model.user.UserRole;
import com.ytu.gymbackend.service.ChargeProfileService;
import com.ytu.gymbackend.service.session.UserSessionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/charge-profile")
public class ChargeProfileController {
    private final UserSessionService userSessionService;
    private final ChargeProfileService chargeProfileService;

    public ChargeProfileController(UserSessionService userSessionService, ChargeProfileService chargeProfileService) {
        this.userSessionService = userSessionService;
        this.chargeProfileService = chargeProfileService;
    }

    @PostMapping("/create")
    public ResponseEntity<ChargeProfileResponse> createChargeProfile(
            @Valid @RequestBody ChargeProfileCreateRequest request
    ) {
        userSessionService.validatePermission(UserRole.ADMIN);

        ChargeProfileResponse response = chargeProfileService.createChargeProfile(request);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChargeProfileResponse> getChargeProfile(
            @PathVariable Long id
    ) {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserRole.ADMIN, UserRole.CLERK)));

        ChargeProfileResponse response = chargeProfileService.getChargeProfile(id);
        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteChargeProfile(
            @PathVariable Long id
    ) {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserRole.ADMIN, UserRole.CLERK)));

        ApiResponse response = chargeProfileService.deleteChargeProfile(id);
        return ResponseEntity.status(200).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChargeProfileResponse> updateChargeProfile(
            @PathVariable Long id,
            @Valid @RequestBody ChargeProfileCreateRequest request
    ) {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserRole.ADMIN, UserRole.CLERK)));

        ChargeProfileResponse response = chargeProfileService.updateChargeProfile(id, request);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/charge-profile")
    public ResponseEntity<List<ChargeProfileResponse>> getAllChargeProfiles() {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserRole.ADMIN, UserRole.CLERK)));

        List<ChargeProfileResponse> response = chargeProfileService.getAllChargeProfiles();
        return ResponseEntity.status(200).body(response);
    }
}
