package com.ytu.gymbackend.model.subscription;

import com.ytu.gymbackend.model.customer.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

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

    @JoinColumn(name = "subscription_model_id")
    @OneToOne
    private SubscriptionModel subscriptionModel;

    @JoinColumn(name = "customer_id", nullable = false)
    @OneToOne
    private Customer customer;

    @CreationTimestamp
    private LocalDate creationDate;

    // If canceled or suspended. Doesn't have to be final.
    private LocalDate endDate;

    @Enumerated(value = EnumType.STRING)
    private SubscriptionStatus status;
}
