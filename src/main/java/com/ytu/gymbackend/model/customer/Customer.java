package com.ytu.gymbackend.model.customer;

import com.ytu.gymbackend.model.subscription.Subscription;
import com.ytu.gymbackend.model.subscription.SubscriptionModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "sur_name")
    private String surName;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Enumerated(value = EnumType.STRING)
    private CustomerStatus customerStatus;

    @CreationTimestamp
    private LocalDateTime accountCreationDate;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private CustomerHealthReport customerHealthReport;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private Subscription subscription;
}
