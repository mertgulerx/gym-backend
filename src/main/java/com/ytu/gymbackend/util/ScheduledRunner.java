package com.ytu.gymbackend.util;

import com.ytu.gymbackend.model.customer.Customer;
import com.ytu.gymbackend.model.customer.CustomerHealthReport;
import com.ytu.gymbackend.model.customer.CustomerHealthReportStatus;
import com.ytu.gymbackend.model.customer.CustomerStatus;
import com.ytu.gymbackend.model.subscription.Subscription;
import com.ytu.gymbackend.model.subscription.SubscriptionPurchase;
import com.ytu.gymbackend.model.subscription.SubscriptionStatus;
import com.ytu.gymbackend.repository.CustomerHealthReportRepository;
import com.ytu.gymbackend.repository.CustomerRepository;
import com.ytu.gymbackend.repository.SubscriptionPurchaseRepository;
import com.ytu.gymbackend.repository.SubscriptionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ScheduledRunner {

    private final SubscriptionPurchaseRepository subscriptionPurchaseRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final CustomerHealthReportRepository customerHealthReportRepository;
    private final CustomerRepository customerRepository;

    public ScheduledRunner(SubscriptionPurchaseRepository subscriptionPurchaseRepository, SubscriptionRepository subscriptionRepository, CustomerHealthReportRepository customerHealthReportRepository, CustomerRepository customerRepository) {
        this.subscriptionPurchaseRepository = subscriptionPurchaseRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.customerHealthReportRepository = customerHealthReportRepository;
        this.customerRepository = customerRepository;
    }

    @Scheduled(cron = "* * 0 * * *")
    public void expirePurchases() {
        List<SubscriptionPurchase> subscriptionPurchaseList = subscriptionPurchaseRepository.findAllByIsCompleted(false);
        LocalDate currentDate = LocalDate.now().plusDays(1);

        for (SubscriptionPurchase subscriptionPurchase : subscriptionPurchaseList){
            if (currentDate.isBefore(subscriptionPurchase.getCreationDate().plusMonths(subscriptionPurchase.getSubscriptionMonthPeriod()))){
                subscriptionPurchase.setIsCompleted(true);
                subscriptionPurchaseRepository.save(subscriptionPurchase);
                Subscription subscription = subscriptionPurchase.getSubscription();
                subscription.setStatus(SubscriptionStatus.EXPIRED);
                subscriptionRepository.save(subscription);
            }
        }
    }
    @Scheduled(cron = "* * 1 * * *")
    public void expireHealthReport(){
        List<CustomerHealthReport> customerHealthReportList = customerHealthReportRepository.findAllByEndDateBefore(LocalDate.now());

        for (CustomerHealthReport customerHealthReport : customerHealthReportList){
            customerHealthReport.setCustomerHealthReportStatus(CustomerHealthReportStatus.EXPIRED);
            customerHealthReportRepository.save(customerHealthReport);

            Customer customer = customerHealthReport.getCustomer();
            customer.setCustomerStatus(CustomerStatus.PENDING);
            customerRepository.save(customer);
        }
    }
}
