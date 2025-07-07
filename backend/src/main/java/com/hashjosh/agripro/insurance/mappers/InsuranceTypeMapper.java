package com.hashjosh.agripro.insurance.mappers;

import com.hashjosh.agripro.insurance.dto.InsuranceTypeRequestDto;
import com.hashjosh.agripro.insurance.dto.InsuranceTypeResponseDto;
import com.hashjosh.agripro.insurance.models.InsuranceType;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class InsuranceTypeMapper {
    public InsuranceTypeResponseDto toInsuraceTypeResponseDto(InsuranceType insuranceType) {
        return new InsuranceTypeResponseDto(insuranceType.getId(), insuranceType.getDisplayName(),insuranceType.getDescription(),
                insuranceType.isRequiredAiAnalyses(),insuranceType.getCreatedAt(),insuranceType.getUpdatedAt());
    }


    public InsuranceType dtoToInsuranceType(InsuranceTypeRequestDto dto) {
        return InsuranceType.builder()
                .description(dto.description())
                .displayName(dto.displayName())
                .requiredAiAnalyses(dto.requiredAiAnalyses())
                .createdAt(new Timestamp(new Date().getTime()))
                .build();
    }
}
