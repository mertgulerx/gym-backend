package com.ytu.gymbackend.repository;

import com.ytu.gymbackend.model.subscription.SubscriptionPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionPurchaseRepository extends JpaRepository<SubscriptionPurchase, Long> {
}
