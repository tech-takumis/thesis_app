package com.hashjosh.agripro.insurance.services;

import com.hashjosh.agripro.exception.InvalidApplicationException;
import com.hashjosh.agripro.insurance.dto.InsuranceFieldRequestDto;
import com.hashjosh.agripro.insurance.dto.InsuranceFieldResponseDto;
import com.hashjosh.agripro.insurance.enums.Datatype;
import com.hashjosh.agripro.insurance.mappers.InsuranceFieldMapper;
import com.hashjosh.agripro.insurance.models.FileMetadata;
import com.hashjosh.agripro.insurance.models.InsuranceField;
import com.hashjosh.agripro.insurance.repository.InsuranceFieldRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InsuranceFieldService {
    private final InsuranceFieldRepository repository;
    private final InsuranceFieldMapper mapper;

    public InsuranceFieldService(InsuranceFieldRepository repository, InsuranceFieldMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public InsuranceFieldResponseDto saveInsuranceField(InsuranceFieldRequestDto field) {
        FileMetadata fileMetadata = null;

        if(field.fieldType() == Datatype.File){
            fileMetadata = mapper.toFileMetadata(field);
        }

        InsuranceField insuranceField = mapper.toInsuranceField(field, fileMetadata);

        return mapper.toInsuranceFieldResponse(repository.save(insuranceField));
    }


    public List<InsuranceFieldResponseDto> findAll() {
            return repository.findAll().stream().map(mapper::toInsuranceFieldResponse)
                    .collect(Collectors.toList());
    }

    public String delete(Long id) {
        InsuranceField insuranceField = repository.findById(id).orElseThrow(
                () -> new InvalidApplicationException("Insurance field not found")
        );
        repository.delete(insuranceField);
        return "Insurnace field id::: "+ id + " deleted";
    }

    public Optional<InsuranceFieldResponseDto> findById(Long id) {
        return repository.findById(id).map(mapper::toInsuranceFieldResponse);
    }
}
