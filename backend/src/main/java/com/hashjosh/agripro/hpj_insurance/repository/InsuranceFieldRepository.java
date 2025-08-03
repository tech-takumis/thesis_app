package com.hashjosh.agripro.hpj_insurance.repository;

import com.hashjosh.agripro.hpj_insurance.model.InsuranceField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InsuranceFieldRepository extends JpaRepository<InsuranceField,Long> {
   Optional<List<InsuranceField>> findByInsuranceType_Id(Long id);
}
