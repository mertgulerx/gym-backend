package com.ytu.gymbackend.util;

import com.ytu.gymbackend.model.subscription.Subscription;
import com.ytu.gymbackend.model.subscription.SubscriptionPurchase;
import com.ytu.gymbackend.model.subscription.SubscriptionStatus;
import com.ytu.gymbackend.repository.SubscriptionPurchaseRepository;
import com.ytu.gymbackend.repository.SubscriptionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ScheduledPurchaseExpiration {

    private final SubscriptionPurchaseRepository subscriptionPurchaseRepository;
    private final SubscriptionRepository subscriptionRepository;

    public ScheduledPurchaseExpiration(SubscriptionPurchaseRepository subscriptionPurchaseRepository, SubscriptionRepository subscriptionRepository) {
        this.subscriptionPurchaseRepository = subscriptionPurchaseRepository;
        this.subscriptionRepository = subscriptionRepository;
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
}
