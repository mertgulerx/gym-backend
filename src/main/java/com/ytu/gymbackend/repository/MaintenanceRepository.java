package com.ytu.gymbackend.repository;

import com.ytu.gymbackend.model.machine.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
    List<Maintenance> findAllByCreationDateAfterAndCreationDateBefore(LocalDate creationDateAfter, LocalDate creationDateBefore);
}
