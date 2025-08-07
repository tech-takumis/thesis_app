package com.hashjosh.agripro.insurance.repository;

import com.hashjosh.agripro.insurance.model.InsuranceSection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsuranceSectionRepository extends JpaRepository<InsuranceSection,Long> {
}
