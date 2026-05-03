package com.ytu.gymbackend.model.customer;

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

    @Column(name = "tc_kimlik_no_encrypted", nullable = false, columnDefinition = "VARCHAR(255)")
    private String tcKimlikNoEncrypted;

    @Column(name = "tc_kimlik_no_index", nullable = false, unique = true, columnDefinition = "CHAR(64)")
    private String tcKimlikNoIndex;

    @Column(name = "name")
    private String name;

    @Column(name = "sur_name")
    private String surName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Enumerated(value = EnumType.STRING)
    private CustomerStatus customerStatus;

    @CreationTimestamp
    private LocalDateTime accountCreationDate;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CustomerHealthReport customerHealthReport;
}
