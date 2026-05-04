package com.ytu.gymbackend.controller;

import com.ytu.gymbackend.dto.ApiResponse;
import com.ytu.gymbackend.dto.response.ChargeProfileResponse;
import com.ytu.gymbackend.model.user.UserRole;
import com.ytu.gymbackend.service.ChargeProfileService;
import com.ytu.gymbackend.service.SubscriptionService;
import com.ytu.gymbackend.service.session.UserSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/subscription")
public class SubscriptionController {
    private final UserSessionService userSessionService;
    private final ChargeProfileService chargeProfileService;
    private final SubscriptionService subscriptionService;

    public SubscriptionController(UserSessionService userSessionService, ChargeProfileService chargeProfileService, SubscriptionService subscriptionService) {
        this.userSessionService = userSessionService;
        this.chargeProfileService = chargeProfileService;
        this.subscriptionService = subscriptionService;
    }


    @PostMapping("/{customerId}/initialize")
    public ResponseEntity<ApiResponse> initializeCustomerSubscription(
            @PathVariable Long customerId
    ) {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserRole.ADMIN, UserRole.CLERK)));

        ApiResponse response = subscriptionService.initializeCustomerSubscription(customerId);
        return ResponseEntity.status(200).body(response);
    }
}
