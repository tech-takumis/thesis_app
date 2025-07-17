package com.hashjosh.agripro.insurance.services;

import com.hashjosh.agripro.exception.InvalidApplicationException;
import com.hashjosh.agripro.insurance.dto.InsuranceApplicationRequestDto;
import com.hashjosh.agripro.insurance.dto.InsuranceRequestDto;
import com.hashjosh.agripro.insurance.mappers.ApplicationInsuranceMapper;
import com.hashjosh.agripro.insurance.models.InsuranceApplication;
import com.hashjosh.agripro.insurance.models.InsuranceField;
import com.hashjosh.agripro.insurance.models.InsuranceType;
import com.hashjosh.agripro.insurance.repository.ApplicationInsuranceRepository;
import com.hashjosh.agripro.insurance.repository.InsuranceFieldRepository;
import com.hashjosh.agripro.insurance.repository.InsuranceTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InternalInsuranceService {
    private final ApplicationInsuranceRepository repository;
    private  final InsuranceTypeRepository insuranceTypeRepository;
    private final InsuranceFieldRepository insuranceFieldRepository;

    public InternalInsuranceService(ApplicationInsuranceRepository repository,
                                    InsuranceTypeRepository insuranceTypeRepository,
                                    InsuranceFieldRepository insuranceFieldRepository) {
        this.repository = repository;
        this.insuranceTypeRepository = insuranceTypeRepository;
        this.insuranceFieldRepository = insuranceFieldRepository;
    }

    @Transactional
    public  InsuranceType saveInsuranceType(InsuranceType type) {
        return insuranceTypeRepository.save(type);
    }
    @Transactional
    public void saveInsuranceFields(List<InsuranceField> fields) {
        insuranceFieldRepository.saveAll(fields);
    }

    @Transactional
    public void saveInsuranceApplication(InsuranceApplication application) {
        repository.save(application);
        repository.flush();
    }


    public InsuranceType findInsurance(Long id) {
        return insuranceTypeRepository.findById(id)
                .orElseThrow(() -> new InvalidApplicationException("Invalid application id:::"+ id));
    }
}
