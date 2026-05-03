package com.ytu.gymbackend.repository;

import com.ytu.gymbackend.model.customer.CustomerHealthReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerHealthReportRepository extends JpaRepository<CustomerHealthReport, Long> {
}
