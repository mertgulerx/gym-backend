package com.ytu.gymbackend.service;

import com.ytu.gymbackend.dto.ApiResponse;
import com.ytu.gymbackend.exception.BadRequestException;
import com.ytu.gymbackend.exception.NotFoundException;
import com.ytu.gymbackend.model.customer.Customer;
import com.ytu.gymbackend.model.customer.CustomerStatus;
import com.ytu.gymbackend.model.subscription.Subscription;
import com.ytu.gymbackend.model.subscription.SubscriptionStatus;
import com.ytu.gymbackend.repository.CustomerRepository;
import com.ytu.gymbackend.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionServiceImpl implements SubscriptionService{
    private final CustomerRepository customerRepository;
    private final SubscriptionService subscriptionService;
    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionServiceImpl(CustomerRepository customerRepository, SubscriptionService subscriptionService, SubscriptionRepository subscriptionRepository) {
        this.customerRepository = customerRepository;
        this.subscriptionService = subscriptionService;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public ApiResponse initializeCustomerSubscription(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new NotFoundException("customer_not_found"));

        if (!customer.getCustomerStatus().equals(CustomerStatus.VERIFIED)){
            throw new BadRequestException("customer_is_not_verified");
        }

        if (customer.getSubscription() != null){
            throw  new BadRequestException("customer_already_has_subscription");
        }

        Subscription subscription = new Subscription();
        subscription.setCustomer(customer);
        subscription.setStatus(SubscriptionStatus.NO_PURCHASE_YET);
        subscriptionRepository.save(subscription);

        return new ApiResponse(true, "subscription_initialized_successfuly");
    }
}
