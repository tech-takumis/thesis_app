package com.hashjosh.agripro.insurance.services;

import com.hashjosh.agripro.insurance.dto.InsuranceTypeRequestDto;
import com.hashjosh.agripro.insurance.dto.InsuranceTypeResponseDto;
import com.hashjosh.agripro.insurance.mappers.InsuranceTypeMapper;
import com.hashjosh.agripro.insurance.models.InsuranceType;
import com.hashjosh.agripro.insurance.repository.InsuranceTypeRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InsuranceTypeService {
    private final InsuranceTypeMapper mapper;
    private final InsuranceTypeRepository repository;

    public InsuranceTypeService(InsuranceTypeMapper mapper, InsuranceTypeRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    public List<InsuranceTypeResponseDto> getAll() {

        return repository.findAll().stream()
                .map(mapper::toInsuraceTypeResponseDto)
                .collect(Collectors.toList());
    }


    public InsuranceTypeResponseDto savedInsurance(InsuranceTypeRequestDto dto) {
        InsuranceType  insuranceType = mapper.dtoToInsuranceType(dto);

        return mapper.toInsuraceTypeResponseDto(repository.save(insuranceType));
    }


    public ResponseEntity<?> findById(Long id) {
        InsuranceType insuranceType = repository.findById(id).orElse(null);

        if(insuranceType == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(mapper.toInsuraceTypeResponseDto(insuranceType));
    }

    public ResponseEntity<?> update(Long id,InsuranceTypeRequestDto dto) {
        InsuranceType insurance = repository.findById(id).orElse(null);
       if(insurance == null) {
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }
       InsuranceType updatedInsurance = new InsuranceType();
        updatedInsurance.setId(id);
        updatedInsurance.setDisplayName(dto.displayName());
        updatedInsurance.setDescription(dto.description());
        updatedInsurance.setRequiredAiAnalyses(dto.requiredAiAnalyses());
        updatedInsurance.setCreatedAt(insurance.getCreatedAt());
        updatedInsurance.setUpdatedAt(new Timestamp(new Date().getTime()));

       return new ResponseEntity<>(mapper.toInsuraceTypeResponseDto(repository.save(updatedInsurance)), HttpStatus.OK);

    }
}
