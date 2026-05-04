package com.ytu.gymbackend.model.machine;

import com.ytu.gymbackend.model.user.User;
import jakarta.persistence.*;
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
@Table(name = "machine_repairs")
public class Repair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "machine_id", nullable = false)
    private Machine machine;

    @CreationTimestamp
    private LocalDate sentDate;

    private LocalDate completeDate;

    private Integer estimatedReturnDays;

    @JoinColumn(name = "maintainer_id", nullable = false)
    @OneToOne
    private User maintainer;

    private BigDecimal cost;

    private String info;

    private Boolean isCompleted;
}
