package com.ytu.gymbackend.service;

import com.ytu.gymbackend.dto.ApiResponse;
import com.ytu.gymbackend.dto.request.ChargeProfileCreateRequest;
import com.ytu.gymbackend.dto.response.ChargeProfileResponse;
import com.ytu.gymbackend.dto.response.CustomerResponse;
import com.ytu.gymbackend.exception.NotFoundException;
import com.ytu.gymbackend.model.customer.Customer;
import com.ytu.gymbackend.model.subscription.ChargeProfile;
import com.ytu.gymbackend.model.subscription.Subscription;
import com.ytu.gymbackend.model.subscription.SubscriptionStatus;
import com.ytu.gymbackend.repository.ChargeProfileRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    private final ChargeProfileRepository chargeProfileRepository;

    public SubscriptionServiceImpl(ChargeProfileRepository chargeProfileRepository) {
        this.chargeProfileRepository = chargeProfileRepository;
    }

    @Override
    public ChargeProfileResponse createChargeProfile(ChargeProfileCreateRequest request) {
        ChargeProfile chargeProfile = new ChargeProfile();
        chargeProfile.setChargeCost(request.getChargeCost());
        chargeProfile.setChargeRate(request.getChargeRate());
        chargeProfile.setInfo(request.getInfo());
        chargeProfile.setTitle(request.getTitle());
        chargeProfile = chargeProfileRepository.save(chargeProfile);

        ChargeProfileResponse chargeProfileResponse = new ChargeProfileResponse();
        chargeProfileResponse.setId(chargeProfile.getId());
        chargeProfileResponse.setInfo(chargeProfile.getInfo());
        chargeProfileResponse.setTitle(chargeProfile.getTitle());
        chargeProfileResponse.setChargeCost(chargeProfile.getChargeCost());
        chargeProfileResponse.setChargeRate(chargeProfile.getChargeRate());
        chargeProfileResponse.setCreationDate(chargeProfile.getCreationTime().toString());

        return chargeProfileResponse;
    }

    @Override
    public ChargeProfileResponse getChargeProfile(Long id) {
        ChargeProfile chargeProfile = chargeProfileRepository.findById(id).orElseThrow(() -> new NotFoundException("charge_profile_not_found"));

        ChargeProfileResponse chargeProfileResponse = new ChargeProfileResponse();
        chargeProfileResponse.setId(chargeProfile.getId());
        chargeProfileResponse.setInfo(chargeProfile.getInfo());
        chargeProfileResponse.setTitle(chargeProfile.getTitle());
        chargeProfileResponse.setChargeCost(chargeProfile.getChargeCost());
        chargeProfileResponse.setChargeRate(chargeProfile.getChargeRate());
        chargeProfileResponse.setCreationDate(chargeProfile.getCreationTime().toString());

        return chargeProfileResponse;
    }

    @Override
    public List<ChargeProfileResponse> getAllChargeProfiles() {
        List<ChargeProfile> allChargeProfiles = chargeProfileRepository.findAll();

        if (allChargeProfiles.isEmpty()){
            throw new NotFoundException("charge_profile_not_found");
        }

        List<ChargeProfileResponse> chargeProfileResponseList = new ArrayList<>();

        for (ChargeProfile chargeProfile : allChargeProfiles){
            ChargeProfileResponse chargeProfileResponse = new ChargeProfileResponse();
            chargeProfileResponse.setId(chargeProfile.getId());
            chargeProfileResponse.setInfo(chargeProfile.getInfo());
            chargeProfileResponse.setTitle(chargeProfile.getTitle());
            chargeProfileResponse.setChargeCost(chargeProfile.getChargeCost());
            chargeProfileResponse.setChargeRate(chargeProfile.getChargeRate());
            chargeProfileResponse.setCreationDate(chargeProfile.getCreationTime().toString());
            chargeProfileResponseList.add(chargeProfileResponse);
        }

        return chargeProfileResponseList;
    }

    @Override
    public ChargeProfileResponse updateChargeProfile(Long id, ChargeProfileCreateRequest request) {
        ChargeProfile chargeProfile = chargeProfileRepository.findById(id).orElseThrow(() -> new NotFoundException("charge_profile_not_found"));

        chargeProfile.setChargeCost(request.getChargeCost());
        chargeProfile.setChargeRate(request.getChargeRate());
        chargeProfile.setInfo(request.getInfo());
        chargeProfile.setTitle(request.getTitle());
        chargeProfile = chargeProfileRepository.save(chargeProfile);

        ChargeProfileResponse chargeProfileResponse = new ChargeProfileResponse();
        chargeProfileResponse.setId(chargeProfile.getId());
        chargeProfileResponse.setInfo(chargeProfile.getInfo());
        chargeProfileResponse.setTitle(chargeProfile.getTitle());
        chargeProfileResponse.setChargeCost(chargeProfile.getChargeCost());
        chargeProfileResponse.setChargeRate(chargeProfile.getChargeRate());
        chargeProfileResponse.setCreationDate(chargeProfile.getCreationTime().toString());

        return chargeProfileResponse;
    }

    @Override
    public ApiResponse deleteChargeProfile(Long id) {
        ChargeProfile chargeProfile = chargeProfileRepository.findById(id).orElseThrow(() -> new NotFoundException("charge_profile_not_found"));
        chargeProfileRepository.delete(chargeProfile);
        return new ApiResponse(true, "charge_profile_deleted_successfuly");
    }
}
