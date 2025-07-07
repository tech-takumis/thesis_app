package com.hashjosh.agripro.user.repository;

import com.hashjosh.agripro.user.models.StaffProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffProfileRepository extends JpaRepository<StaffProfile, Long> {
}
