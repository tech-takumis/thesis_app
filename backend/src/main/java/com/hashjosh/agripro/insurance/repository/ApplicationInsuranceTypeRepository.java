package com.hashjosh.agripro.insurance.repository;

import com.hashjosh.agripro.insurance.models.InsuranceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationInsuranceTypeRepository extends JpaRepository<InsuranceType, Long> {
}
