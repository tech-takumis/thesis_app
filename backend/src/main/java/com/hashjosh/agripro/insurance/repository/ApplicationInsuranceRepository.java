package com.hashjosh.agripro.insurance.repository;

import com.hashjosh.agripro.insurance.models.InsuranceApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationInsuranceRepository extends JpaRepository<InsuranceApplication, Long> {

    List<InsuranceApplication> findAllByUserId(Long userId);

}
