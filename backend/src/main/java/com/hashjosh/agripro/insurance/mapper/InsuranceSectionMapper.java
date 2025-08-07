package com.hashjosh.agripro.insurance.mapper;

import com.hashjosh.agripro.insurance.dto.InsuranceRequestDto;
import com.hashjosh.agripro.insurance.dto.InsuranceSectionRequestDto;
import com.hashjosh.agripro.insurance.dto.InsuranceSectionResponseDto;
import com.hashjosh.agripro.insurance.model.Insurance;
import com.hashjosh.agripro.insurance.model.InsuranceSection;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InsuranceSectionMapper {

    private final InsuranceFieldMapper fieldMapper;

    public InsuranceSectionMapper(InsuranceFieldMapper fieldMapper) {
        this.fieldMapper = fieldMapper;
    }

    public InsuranceSectionResponseDto toInsuranceSectionResponse(InsuranceSection section){
        return new InsuranceSectionResponseDto(
                section.getId(),
                section.getTitle(),
                section.getInsuranceField().stream().map(
                        fieldMapper::toInsuranceFieldResponse
                ).collect(Collectors.toList())
        );
    }

    public List<InsuranceSection> toListInsuranceSectionDto(Insurance savedInsurance, InsuranceRequestDto dto) {
        return dto.sections().stream().map(
                section ->  InsuranceSection.builder()
                        .title(section.title())
                        .insurance(savedInsurance)
                        .build()
        ).collect(Collectors.toList());
    }

    public InsuranceSection toInsuranceSection(InsuranceSectionRequestDto section, Insurance savedInsurance) {
        return InsuranceSection.builder()
                .title(section.title())
                .insurance(savedInsurance)
                .build();
    }
}
