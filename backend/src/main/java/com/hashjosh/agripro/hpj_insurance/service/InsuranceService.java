package com.hashjosh.agripro.hpj_insurance.service;

import com.hashjosh.agripro.hpj_insurance.dto.InsuranceRequestDto;
import com.hashjosh.agripro.hpj_insurance.dto.InsuranceResponseDto;
import com.hashjosh.agripro.hpj_insurance.exception.InsuranceException;
import com.hashjosh.agripro.hpj_insurance.mapper.InsuranceMapper;
import com.hashjosh.agripro.hpj_insurance.model.InsuranceField;
import com.hashjosh.agripro.hpj_insurance.model.InsuranceType;
import com.hashjosh.agripro.hpj_insurance.repository.InsuranceFieldRepository;
import com.hashjosh.agripro.hpj_insurance.repository.InsuranceTypeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InsuranceService {

    private final InsuranceFieldRepository insuranceFieldRepository;
    private final InsuranceTypeRepository insuranceTypeRepository;
    private final InsuranceMapper insuranceMapper;

    public InsuranceService(InsuranceFieldRepository insuranceFieldRepository, InsuranceTypeRepository insuranceTypeRepository, InsuranceMapper insuranceMapper) {
        this.insuranceFieldRepository = insuranceFieldRepository;
        this.insuranceTypeRepository = insuranceTypeRepository;
        this.insuranceMapper = insuranceMapper;
    }

    public InsuranceResponseDto save(InsuranceRequestDto dto) {

        InsuranceType savedInsuranceType = insuranceTypeRepository.save(insuranceMapper.toInsuranceType(dto));
        List<InsuranceField> fields = insuranceFieldRepository.saveAll(insuranceMapper.toInsuranceField(dto, savedInsuranceType));
        savedInsuranceType.setInsuranceField(fields);
        return insuranceMapper.toInsuranceResponse(savedInsuranceType);
    }

    public List<InsuranceResponseDto> findAll() {
        List<InsuranceType> insuranceTypes = insuranceTypeRepository.findAll();

        return insuranceTypes.stream()
                .map(insuranceMapper::toInsuranceResponse)
                .collect(Collectors.toList());

    }

    public InsuranceResponseDto findById(Long id) {
        InsuranceType insurance = insuranceTypeRepository.findById(id)
                .orElseThrow(() -> new InsuranceException(
                        "Insurance id "+ id+" does not exist",
                        HttpStatus.NOT_FOUND.value(),
                        "Insurance does not exist"
                ));
        return insuranceMapper.toInsuranceResponse(insurance);
    }
}
