package com.ytu.gymbackend.service;

import com.ytu.gymbackend.dto.ApiResponse;
import com.ytu.gymbackend.dto.request.SubscriptionPurchaseRequest;
import com.ytu.gymbackend.dto.response.SubscriptionPurchaseResponse;
import com.ytu.gymbackend.dto.response.SubscriptionResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SubscriptionService {
    ApiResponse initializeCustomerSubscription(Long customerId);

    SubscriptionPurchaseResponse createSubscriptionPurchase(Long customerId, @Valid SubscriptionPurchaseRequest request);

    SubscriptionResponse getCustomerSubscription(Long customerId);

    SubscriptionPurchaseResponse getLastCustomerSubscriptionPurchase(Long customerId);

    List<SubscriptionPurchaseResponse> getAllCustomerSubscriptionPurchases(Long customerId);

    ApiResponse cancelCustomerSubscription(Long customerId);
}
