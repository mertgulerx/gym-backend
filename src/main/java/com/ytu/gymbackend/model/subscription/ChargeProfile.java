package com.ytu.gymbackend.model.subscription;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "charge_profiles")
public class ChargeProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String info;

    @Column(nullable = false)
    private BigDecimal chargeRate;

    @Column(nullable = false)
    private BigDecimal chargeCost;

    @CreationTimestamp
    private LocalDateTime creationTime;
}
