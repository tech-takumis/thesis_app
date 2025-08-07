package com.hashjosh.agripro.insurance.repository;

import com.hashjosh.agripro.insurance.model.InsuranceApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsuranceApplicationRepository extends JpaRepository<InsuranceApplication,Long> {
}
