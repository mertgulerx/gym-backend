package com.ytu.gymbackend.controller;

import com.ytu.gymbackend.dto.ApiResponse;
import com.ytu.gymbackend.dto.request.SubscriptionPurchaseRequest;
import com.ytu.gymbackend.dto.response.SubscriptionPurchaseResponse;
import com.ytu.gymbackend.dto.response.SubscriptionResponse;
import com.ytu.gymbackend.model.user.UserRole;
import com.ytu.gymbackend.service.ChargeProfileService;
import com.ytu.gymbackend.service.SubscriptionService;
import com.ytu.gymbackend.service.session.UserSessionService;
import jakarta.validation.Valid;
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

    @PostMapping("/{customerId}/purchase/create")
    public ResponseEntity<SubscriptionPurchaseResponse> createSubscriptionPurchase(
            @PathVariable Long customerId,
            @Valid @RequestBody SubscriptionPurchaseRequest request
    ) {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserRole.ADMIN, UserRole.CLERK)));

        SubscriptionPurchaseResponse response = subscriptionService.createSubscriptionPurchase(customerId, request);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<SubscriptionResponse> getCustomerSubscription(
            @PathVariable Long customerId
    ) {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserRole.ADMIN, UserRole.CLERK)));

        SubscriptionResponse response = subscriptionService.getCustomerSubscription(customerId);
        return ResponseEntity.status(200).body(response);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<ApiResponse> cancelCustomerSubscription(
            @PathVariable Long customerId
    ) {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserRole.ADMIN, UserRole.CLERK)));

        ApiResponse response = subscriptionService.cancelCustomerSubscription(customerId);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/{customerId}/last")
    public ResponseEntity<SubscriptionPurchaseResponse> getLastCustomerSubscriptionPurchase(
            @PathVariable Long customerId
    ) {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserRole.ADMIN, UserRole.CLERK)));

        SubscriptionPurchaseResponse response = subscriptionService.getLastCustomerSubscriptionPurchase(customerId);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/{customerId}/all")
    public ResponseEntity<List<SubscriptionPurchaseResponse>> getAllCustomerSubscriptionPurchases(
            @PathVariable Long customerId
    ) {
        userSessionService.validatePermission(new ArrayList<>(List.of(UserRole.ADMIN, UserRole.CLERK)));

        List<SubscriptionPurchaseResponse> response = subscriptionService.getAllCustomerSubscriptionPurchases(customerId);
        return ResponseEntity.status(200).body(response);
    }
}
