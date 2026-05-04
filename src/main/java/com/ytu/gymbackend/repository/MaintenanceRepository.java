package com.ytu.gymbackend.repository;

import com.ytu.gymbackend.model.machine.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
}
