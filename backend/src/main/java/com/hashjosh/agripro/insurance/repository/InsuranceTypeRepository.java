package com.hashjosh.agripro.insurance.repository;


import com.hashjosh.agripro.insurance.models.InsuranceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsuranceTypeRepository extends JpaRepository<InsuranceType, Long> {
}
