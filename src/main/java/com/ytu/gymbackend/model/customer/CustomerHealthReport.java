package com.ytu.gymbackend.model.customer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customer_health_reports")
public class CustomerHealthReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    CustomerHealthReportStatus customerHealthReportStatus;

    @Column(name = "revision_date")
    private LocalDate revisionDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Lob
    @Column(name = "pdf_data", columnDefinition = "BLOB")
    private byte[] pdfData;
}
