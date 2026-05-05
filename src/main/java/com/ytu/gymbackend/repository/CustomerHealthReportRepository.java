package com.ytu.gymbackend.repository;

import com.ytu.gymbackend.model.customer.CustomerHealthReport;
import com.ytu.gymbackend.model.customer.CustomerHealthReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CustomerHealthReportRepository extends JpaRepository<CustomerHealthReport, Long> {
    List<CustomerHealthReport> findAllByEndDateBefore(LocalDate endDateBefore);

    List<CustomerHealthReport> findAllByCustomerHealthReportStatusEquals(CustomerHealthReportStatus customerHealthReportStatus);
}
