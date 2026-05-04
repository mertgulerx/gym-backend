package com.ytu.gymbackend.repository;

import com.ytu.gymbackend.model.machine.Repair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RepairRepository extends JpaRepository<Repair, Long> {
    List<Repair> findAllBySentDateAfterAndSentDateBefore(LocalDate sentDateAfter, LocalDate sentDateBefore);
}
