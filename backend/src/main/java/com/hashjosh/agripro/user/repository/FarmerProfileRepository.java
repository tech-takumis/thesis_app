package com.hashjosh.agripro.user.repository;

import com.hashjosh.agripro.user.models.FarmerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmerProfileRepository extends JpaRepository<FarmerProfile, Long> {
}
