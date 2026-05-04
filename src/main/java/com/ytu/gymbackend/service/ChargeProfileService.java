package com.ytu.gymbackend.service;

import com.ytu.gymbackend.dto.ApiResponse;
import com.ytu.gymbackend.dto.request.ChargeProfileCreateRequest;
import com.ytu.gymbackend.dto.response.ChargeProfileResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChargeProfileService {
    ChargeProfileResponse createChargeProfile(@Valid ChargeProfileCreateRequest request);

    ChargeProfileResponse getChargeProfile(Long id);

    List<ChargeProfileResponse> getAllChargeProfiles();

    ChargeProfileResponse updateChargeProfile(Long id, @Valid ChargeProfileCreateRequest request);

    ApiResponse deleteChargeProfile(Long id);
}
