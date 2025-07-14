package com.hashjosh.agripro.insurance.repository;

import com.hashjosh.agripro.insurance.models.InsuranceApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationInsuranceRepository extends JpaRepository<InsuranceApplication, Long> {

}
