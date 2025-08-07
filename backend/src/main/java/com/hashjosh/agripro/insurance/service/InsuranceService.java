package com.hashjosh.agripro.insurance.service;

import com.hashjosh.agripro.insurance.dto.*;
import com.hashjosh.agripro.insurance.exception.InsuranceException;
import com.hashjosh.agripro.insurance.mapper.InsuranceFieldMapper;
import com.hashjosh.agripro.insurance.mapper.InsuranceMapper;
import com.hashjosh.agripro.insurance.mapper.InsuranceSectionMapper;
import com.hashjosh.agripro.insurance.model.InsuranceField;
import com.hashjosh.agripro.insurance.model.Insurance;
import com.hashjosh.agripro.insurance.model.InsuranceSection;
import com.hashjosh.agripro.insurance.repository.InsuranceFieldRepository;
import com.hashjosh.agripro.insurance.repository.InsuranceSectionRepository;
import com.hashjosh.agripro.insurance.repository.InsuranceTypeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InsuranceService {

    private final InsuranceFieldRepository insuranceFieldRepository;
    private final InsuranceTypeRepository insuranceTypeRepository;
    private final InsuranceMapper insuranceMapper;
    private final InsuranceSectionMapper sectionMapper;
    private final InsuranceFieldMapper fieldMapper;
    private final InsuranceSectionRepository insuranceSectionRepository;
    private final InsuranceFieldMapper insuranceFieldMapper;
    private final InsuranceSectionMapper insuranceSectionMapper;

    public InsuranceService(InsuranceFieldRepository insuranceFieldRepository, InsuranceTypeRepository insuranceTypeRepository, InsuranceMapper insuranceMapper, InsuranceSectionMapper sectionMapper, InsuranceFieldMapper fieldMapper, InsuranceSectionRepository insuranceSectionRepository, InsuranceFieldMapper insuranceFieldMapper, InsuranceSectionMapper insuranceSectionMapper) {
        this.insuranceFieldRepository = insuranceFieldRepository;
        this.insuranceTypeRepository = insuranceTypeRepository;
        this.insuranceMapper = insuranceMapper;
        this.sectionMapper = sectionMapper;
        this.fieldMapper = fieldMapper;
        this.insuranceSectionRepository = insuranceSectionRepository;
        this.insuranceFieldMapper = insuranceFieldMapper;
        this.insuranceSectionMapper = insuranceSectionMapper;
    }

    public InsuranceResponseDto save(InsuranceRequestDto dto) {
        // Convert insurance dto to insurance object
        Insurance toInsurance = insuranceMapper.toInsurance(dto);
        // save the insurance
        Insurance savedInsurance = insuranceTypeRepository.save(toInsurance);

        List<InsuranceSection> savedSections = new ArrayList<>();
        // lets loop to the list of sections and save each section and its fields
        for(InsuranceSectionRequestDto section: dto.sections()){
            InsuranceSection section1 = sectionMapper.toInsuranceSection(section,savedInsurance);
            InsuranceSection savedSection = insuranceSectionRepository.save(section1);

            List<InsuranceField> toInsuranceField = section.fields().stream().map(
                field -> insuranceFieldMapper.toInsuranceField(field,savedSection)
            ).toList();

            List<InsuranceField> fields = insuranceFieldRepository.saveAll(toInsuranceField);

            savedSection.setInsuranceField(fields);

            savedSections.add(savedSection);
        }

        savedInsurance.setSections(savedSections);

        return insuranceMapper.toInsuranceResponse(insuranceTypeRepository.save(savedInsurance));
    }

    public List<InsuranceResponseDto> findAll() {
        List<Insurance> insurances = insuranceTypeRepository.findAll();

        return insurances.stream()
                .map(insuranceMapper::toInsuranceResponse)
                .collect(Collectors.toList());

    }

    public InsuranceResponseDto findById(Long id) {
        Insurance insurance = insuranceTypeRepository.findById(id)
                .orElseThrow(() -> new InsuranceException(
                        "Insurance id "+ id+" does not exist",
                        HttpStatus.NOT_FOUND.value(),
                        "Insurance does not exist"
                ));
        return insuranceMapper.toInsuranceResponse(insurance);
    }

    public InsuranceResponseDto updateInsuranceType(Long id, InsuranceRequestDto dto) {
       Insurance insurance = insuranceTypeRepository.findById(id)
               .orElseThrow(() -> new InsuranceException(
                       "Insurance id " + id + " not found",
                       HttpStatus.NOT_FOUND.value(),
                       "Insurance does not exist"
               ));

       insurance.setSections(sectionMapper.toListInsuranceSectionDto(insurance,dto));
       insurance.setDescription(dto.description());
       insurance.setRequiredAIAnalysis(dto.requiredAIAnalysis());

       return insuranceMapper.toInsuranceResponse(insuranceTypeRepository.save(insurance));
    }

    public InsuranceFieldResponseDto updateInsuranceFields(Long id, InsuranceFieldDto dto) {
        InsuranceField field = insuranceFieldRepository.findById(id)
                .orElseThrow(() -> new InsuranceException(
                        "Insurance id "+ id+" does not exist",
                        HttpStatus.BAD_REQUEST.value(),
                        "Insurance field does not exist"
                ));

        field.setFieldName(dto.fieldName());
        field.setLabel(dto.label());
        field.setDefaultValue(dto.defaultValue());
        field.setFieldType(dto.fieldType());
        field.setChoices(dto.choices());
        field.setRequired(dto.required());
        field.setValidationRegex(dto.validationRegex());
        field.setKey(dto.key());
        insuranceFieldRepository.save(field);

        return fieldMapper.toInsuranceFieldResponse(field);
    }
}
