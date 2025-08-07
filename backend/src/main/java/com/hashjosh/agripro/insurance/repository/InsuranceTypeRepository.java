package com.hashjosh.agripro.insurance.repository;

import com.hashjosh.agripro.insurance.model.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsuranceTypeRepository extends JpaRepository<Insurance, Long> {
}
