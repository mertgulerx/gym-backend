package com.ytu.gymbackend.model.subscription;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "subscription_models")
public class SubscriptionPurchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @CreationTimestamp
    private LocalDate creationDate;

    private Boolean isCompleted;

    private String title;

    @Max(30)
    @Min(2)
    @Column(nullable = false)
    private Integer subscriptionDays;

    @Min(1)
    @Column(nullable = false)
    private Integer subscriptionMonthPeriod;

    @Column(nullable = false)
    private BigDecimal chargeRate;

    @Column(nullable = false)
    private BigDecimal chargeCost;

    private BigDecimal monthlyCost;

    private BigDecimal totalCost;

    @Column(nullable = false)
    private Boolean isTimeLimited;

    private Integer startHour;

    private Integer endHour;

    public BigDecimal calculateMonthlyCost(){
        return chargeRate.multiply(chargeCost).multiply(BigDecimal.valueOf(subscriptionDays));
    }

    public BigDecimal calculateTotalCost(){
        return chargeRate.multiply(chargeCost).multiply(BigDecimal.valueOf(subscriptionDays)).multiply(BigDecimal.valueOf(subscriptionMonthPeriod));
    }
}
