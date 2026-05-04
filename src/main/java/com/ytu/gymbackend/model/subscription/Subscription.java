package com.ytu.gymbackend.model.subscription;

import com.ytu.gymbackend.model.customer.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.MERGE)
    private List<SubscriptionPurchase> subscriptionPurchaseList = new ArrayList<>();

    @JoinColumn(name = "customer_id", nullable = false)
    @OneToOne
    private Customer customer;

    @CreationTimestamp
    private LocalDate lastSubscriptionStartDate;

    // If canceled or suspended. Doesn't have to exist.
    private LocalDate endDate;

    @Enumerated(value = EnumType.STRING)
    private SubscriptionStatus status;
}
