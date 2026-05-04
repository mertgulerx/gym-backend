package com.ytu.gymbackend.service;

import com.ytu.gymbackend.dto.ApiResponse;
import org.springframework.stereotype.Service;

@Service
public interface SubscriptionService {
    ApiResponse initializeCustomerSubscription(Long customerId);
}
