package com.ytu.gymbackend.service;

import com.ytu.gymbackend.dto.ApiResponse;
import com.ytu.gymbackend.dto.request.SubscriptionPurchaseRequest;
import com.ytu.gymbackend.dto.response.SubscriptionPurchaseResponse;
import com.ytu.gymbackend.dto.response.SubscriptionResponse;
import com.ytu.gymbackend.exception.BadRequestException;
import com.ytu.gymbackend.exception.NotFoundException;
import com.ytu.gymbackend.model.customer.Customer;
import com.ytu.gymbackend.model.customer.CustomerStatus;
import com.ytu.gymbackend.model.subscription.Subscription;
import com.ytu.gymbackend.model.subscription.SubscriptionPurchase;
import com.ytu.gymbackend.model.subscription.SubscriptionStatus;
import com.ytu.gymbackend.repository.CustomerRepository;
import com.ytu.gymbackend.repository.SubscriptionPurchaseRepository;
import com.ytu.gymbackend.repository.SubscriptionRepository;
import com.ytu.gymbackend.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService{
    private final CustomerRepository customerRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPurchaseRepository subscriptionPurchaseRepository;
    private final MapperUtil mapperUtil;

    public SubscriptionServiceImpl(CustomerRepository customerRepository, SubscriptionRepository subscriptionRepository, SubscriptionPurchaseRepository subscriptionPurchaseRepository, MapperUtil mapperUtil) {
        this.customerRepository = customerRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionPurchaseRepository = subscriptionPurchaseRepository;
        this.mapperUtil = mapperUtil;
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

    @Override
    public SubscriptionPurchaseResponse createSubscriptionPurchase(Long customerId, SubscriptionPurchaseRequest request) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new NotFoundException("customer_not_found"));

        if (!customer.getCustomerStatus().equals(CustomerStatus.VERIFIED)){
            throw new BadRequestException("customer_is_not_verified");
        }

        if (customer.getSubscription() == null){
            throw new BadRequestException("customer_subscription_not_initialized");
        }


        if (customer.getSubscription().getSubscriptionPurchaseList().getLast().getIsCompleted() == false){
            throw new BadRequestException("customer_already_has_active_subscription_purchase");
        }

        if (customer.getCustomerHealthReport().getEndDate().isBefore(LocalDate.now().plusMonths(request.getSubscriptionMonthPeriod()))){
            throw new BadRequestException("health_report_expires_before_subscription");
        }

        SubscriptionPurchase subscriptionPurchase = new SubscriptionPurchase();
        subscriptionPurchase.setSubscription(customer.getSubscription());
        subscriptionPurchase.setSubscriptionDays(request.getSubscriptionDays());
        subscriptionPurchase.setSubscriptionMonthPeriod(request.getSubscriptionMonthPeriod());
        subscriptionPurchase.setChargeCost(request.getChargeCost());
        subscriptionPurchase.setChargeRate(request.getChargeRate());
        subscriptionPurchase.setIsCompleted(false);
        subscriptionPurchase.setTitle(request.getTitle());
        subscriptionPurchase.setIsTimeLimited(request.getIsTimeLimited());
        if (request.getIsTimeLimited() && request.getStartHour() != null && request.getEndHour() != null){
            subscriptionPurchase.setStartHour(request.getStartHour());
            subscriptionPurchase.setEndHour(request.getEndHour());
        }

        if (request.getIsTimeLimited() && (request.getStartHour() == null || request.getEndHour() == null)){
            throw new BadRequestException("subscription_purchase_request_time_configuration_is_not_correct");
        }

        subscriptionPurchase.setMonthlyCost(subscriptionPurchase.calculateMonthlyCost());
        subscriptionPurchase.setTotalCost(subscriptionPurchase.calculateTotalCost());
        subscriptionPurchase = subscriptionPurchaseRepository.save(subscriptionPurchase);

        Subscription subscription = customer.getSubscription();
        subscription.setLastSubscriptionStartDate(subscriptionPurchase.getCreationDate());
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscriptionRepository.save(subscription);

        SubscriptionPurchaseResponse subscriptionPurchaseResponse = mapperUtil.map(subscriptionPurchase, SubscriptionPurchaseResponse.class);
        subscriptionPurchaseResponse.setId(subscriptionPurchase.getId());
        subscriptionPurchaseResponse.setSubscriptionId(subscription.getId());
        subscriptionPurchaseResponse.setCreationDate(subscriptionPurchase.getCreationDate().toString());

        return subscriptionPurchaseResponse;
    }

    @Override
    public SubscriptionResponse getCustomerSubscription(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new NotFoundException("customer_not_found"));

        if (!customer.getCustomerStatus().equals(CustomerStatus.VERIFIED)){
            throw new BadRequestException("customer_is_not_verified");
        }

        if (customer.getSubscription() == null){
            throw  new BadRequestException("customer_subscription_not_initialized");
        }

        Subscription subscription = customer.getSubscription();
        SubscriptionResponse subscriptionResponse = new SubscriptionResponse();
        subscriptionResponse.setId(subscription.getId());
        subscriptionResponse.setCustomerId(subscription.getCustomer().getId());

        SubscriptionStatus subscriptionStatus = subscription.getStatus();
        subscriptionResponse.setStatus(subscriptionStatus.toString());

        if (subscriptionStatus.equals(SubscriptionStatus.NO_PURCHASE_YET)){
            subscriptionResponse.setLastSubscriptionStartDate(subscription.getLastSubscriptionStartDate().toString());
        }

        if (subscriptionStatus.equals(SubscriptionStatus.CANCELED) || subscriptionStatus.equals(SubscriptionStatus.SUSPENDED)){
            subscriptionResponse.setEndDate(subscription.getEndDate().toString());
        }

        return subscriptionResponse;
    }

    @Override
    public SubscriptionPurchaseResponse getLastCustomerSubscriptionPurchase(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new NotFoundException("customer_not_found"));

        if (!customer.getCustomerStatus().equals(CustomerStatus.VERIFIED)){
            throw new BadRequestException("customer_is_not_verified");
        }

        if (customer.getSubscription() == null){
            throw new BadRequestException("customer_subscription_not_initialized");
        }

        if (customer.getSubscription().getStatus().equals(SubscriptionStatus.NO_PURCHASE_YET)){
            throw new BadRequestException("customer_doesnt_have_any_purchase");
        }

        List<SubscriptionPurchase> subscriptionPurchaseList = customer.getSubscription().getSubscriptionPurchaseList();

        subscriptionPurchaseList.sort(Comparator.comparing(SubscriptionPurchase::getCreationDate));
        SubscriptionPurchase subscriptionPurchase = subscriptionPurchaseList.getLast();

        SubscriptionPurchaseResponse subscriptionPurchaseResponse = mapperUtil.map(subscriptionPurchase, SubscriptionPurchaseResponse.class);
        subscriptionPurchaseResponse.setId(subscriptionPurchase.getId());
        subscriptionPurchaseResponse.setSubscriptionId(customer.getSubscription().getId());
        subscriptionPurchaseResponse.setCreationDate(subscriptionPurchase.getCreationDate().toString());

        return subscriptionPurchaseResponse;
    }

    @Override
    public List<SubscriptionPurchaseResponse> getAllCustomerSubscriptionPurchases(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new NotFoundException("customer_not_found"));

        if (!customer.getCustomerStatus().equals(CustomerStatus.VERIFIED)){
            throw new BadRequestException("customer_is_not_verified");
        }

        if (customer.getSubscription() == null){
            throw new BadRequestException("customer_subscription_not_initialized");
        }

        if (customer.getSubscription().getStatus().equals(SubscriptionStatus.NO_PURCHASE_YET)){
            throw new BadRequestException("customer_doesnt_have_any_purchase");
        }

        List<SubscriptionPurchase> subscriptionPurchaseList = customer.getSubscription().getSubscriptionPurchaseList();

        subscriptionPurchaseList.sort(Comparator.comparing(SubscriptionPurchase::getCreationDate).reversed());

        List<SubscriptionPurchaseResponse> subscriptionPurchaseResponseList = new ArrayList<>();

        for (SubscriptionPurchase subscriptionPurchase : subscriptionPurchaseList){
            SubscriptionPurchaseResponse subscriptionPurchaseResponse = mapperUtil.map(subscriptionPurchase, SubscriptionPurchaseResponse.class);
            subscriptionPurchaseResponse.setId(subscriptionPurchase.getId());
            subscriptionPurchaseResponse.setSubscriptionId(customer.getSubscription().getId());
            subscriptionPurchaseResponse.setCreationDate(subscriptionPurchase.getCreationDate().toString());
            subscriptionPurchaseResponseList.add(subscriptionPurchaseResponse);
        }

        return subscriptionPurchaseResponseList;
    }

    @Override
    public ApiResponse cancelCustomerSubscription(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new NotFoundException("customer_not_found"));

        if (!customer.getCustomerStatus().equals(CustomerStatus.VERIFIED)){
            throw new BadRequestException("customer_is_not_verified");
        }

        if (customer.getSubscription() == null){
            throw new BadRequestException("customer_subscription_not_initialized");
        }

        if (customer.getSubscription().getStatus().equals(SubscriptionStatus.CANCELED)){
            throw new BadRequestException("customer_subscription_already_canceled");
        }

        if (customer.getSubscription().getStatus().equals(SubscriptionStatus.SUSPENDED)){
            throw new BadRequestException("customer_subscription_is_suspended");
        }

        Subscription subscription = customer.getSubscription();
        List<SubscriptionPurchase> subscriptionPurchaseList = subscription.getSubscriptionPurchaseList();

        subscriptionPurchaseList.sort(Comparator.comparing(SubscriptionPurchase::getCreationDate));

        SubscriptionPurchase lastSubscriptionPurchase = subscriptionPurchaseList.getLast();
        if (lastSubscriptionPurchase.getIsCompleted() != true){
            lastSubscriptionPurchase.setIsCompleted(true);
            subscriptionPurchaseRepository.save(lastSubscriptionPurchase);
        }

        subscription.setStatus(SubscriptionStatus.CANCELED);
        subscriptionRepository.save(subscription);

        return new ApiResponse(true, "subscription_canceled_successfuly");
    }
}
