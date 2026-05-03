package com.ytu.gymbackend.model.machine;

import com.ytu.gymbackend.model.customer.CustomerHealthReport;
import com.ytu.gymbackend.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "machine_maintenances")
public class Maintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "machine_id", nullable = false)
    private Machine machine;

    private LocalDate date;

    @JoinColumn(name = "maintainer_id", nullable = false)
    @OneToOne
    private User maintainer;

    private BigDecimal cost;

    private String info;
}
