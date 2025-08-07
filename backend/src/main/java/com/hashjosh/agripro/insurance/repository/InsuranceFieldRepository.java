package com.hashjosh.agripro.insurance.repository;

import com.hashjosh.agripro.insurance.model.InsuranceField;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsuranceFieldRepository extends JpaRepository<InsuranceField,Long> {
}
