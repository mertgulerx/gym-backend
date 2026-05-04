package com.ytu.gymbackend.repository;

import com.ytu.gymbackend.model.subscription.ChargeProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChargeProfileRepository extends JpaRepository<ChargeProfile, Long> {
}
