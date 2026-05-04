package com.ytu.gymbackend.repository;

import com.ytu.gymbackend.model.subscription.SubscriptionPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SubscriptionPurchaseRepository extends JpaRepository<SubscriptionPurchase, Long> {
    List<SubscriptionPurchase> findAllByIsCompleted(Boolean isCompleted);

    List<SubscriptionPurchase> findAllByCreationDateAfterAndCreationDateBefore(LocalDate creationDateAfter, LocalDate creationDateBefore);
}
