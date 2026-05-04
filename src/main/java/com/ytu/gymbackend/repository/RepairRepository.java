package com.ytu.gymbackend.repository;

import com.ytu.gymbackend.model.machine.Repair;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepairRepository extends JpaRepository<Repair, Long> {
}
