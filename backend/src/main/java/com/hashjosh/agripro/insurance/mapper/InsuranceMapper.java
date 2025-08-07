package com.hashjosh.agripro.insurance.mapper;

import com.hashjosh.agripro.insurance.dto.InsuranceRequestDto;
import com.hashjosh.agripro.insurance.dto.InsuranceResponseDto;
import com.hashjosh.agripro.insurance.dto.InsuranceSectionResponseDto;
import com.hashjosh.agripro.insurance.model.Insurance;
import com.hashjosh.agripro.insurance.model.InsuranceSection;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class InsuranceMapper {

    private final InsuranceFieldMapper fieldMapper;
    private final InsuranceSectionMapper sectionMapper;

    public InsuranceMapper(InsuranceFieldMapper fieldMapper, InsuranceSectionMapper sectionMapper) {
        this.fieldMapper = fieldMapper;
        this.sectionMapper = sectionMapper;
    }

    public InsuranceResponseDto toInsuranceResponse(Insurance insurance) {
        return  new InsuranceResponseDto(
                insurance.getId(),
                insurance.getName(),
                insurance.getLayout(),
                insurance.getDescription(),
                insurance.getRequiredAIAnalysis(),
                insurance.getSections().stream().map(
                        sectionMapper::toInsuranceSectionResponse
                ).collect(Collectors.toList())
        );
    }


    public InsuranceSectionResponseDto toInsuranceSectionResponse(InsuranceSection section) {
        return new InsuranceSectionResponseDto(
                section.getId(),
                section.getTitle(),
                section.getInsuranceField().stream().map(
                        fieldMapper::toInsuranceFieldResponse
                ).collect(Collectors.toList())
        );
    }


    public Insurance toInsurance(InsuranceRequestDto dto) {
        return Insurance.builder()
                .name(dto.name())
                .layout(dto.layout())
                .description(dto.description())
                .requiredAIAnalysis(dto.requiredAIAnalysis())
                .build();
    }
}

