package com.hashjosh.agripro.insurance.mappers;

import com.hashjosh.agripro.insurance.dto.InsuranceApplicationRequestDto;
import com.hashjosh.agripro.insurance.dto.InsuranceRequestDto;
import com.hashjosh.agripro.insurance.dto.InsuranceResponseDto;
import com.hashjosh.agripro.insurance.enums.Status;
import com.hashjosh.agripro.insurance.models.FileMetadata;
import com.hashjosh.agripro.insurance.models.InsuranceApplication;
import com.hashjosh.agripro.insurance.models.InsuranceField;
import com.hashjosh.agripro.insurance.models.InsuranceType;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationInsuranceMapper {

    public InsuranceType toInsuranceType(InsuranceRequestDto dto) {
        return InsuranceType.builder()
                .displayName(dto.displayName())
                .description(dto.description())
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .requiredAiAnalyses(dto.requiredAiAnalyses())
                .build();
    }

    public List<InsuranceField> requestToInsuranceFields(InsuranceRequestDto dto, InsuranceType insuranceType) {
        return dto.fields().stream().map(
                field -> InsuranceField.builder()
                        .key(field.key())
                        .displayName(field.displayName())
                        .fieldType(field.fieldType())
                        .note(field.note())
                        .is_required(field.is_required())
                        .insuranceType(insuranceType)
                        .fileMetadata(FileMetadata.builder()
                                .hasCoordinate(field.hasCoordinate())
                                .coordinate(field.coordinate())
                                .build())
                        .build()
        ).collect(Collectors.toList());
    }

    public InsuranceApplication toInsuranceApplication(InsuranceApplicationRequestDto dto, InsuranceType  insuranceType) {
        return InsuranceApplication.builder()
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .status(Status.PENDING)
                .insuranceType(insuranceType)
                .fieldValues(dto.fieldValues())
                .build();
    }

    public InsuranceResponseDto toInsuranceResponse(InsuranceType insurance) {
        return new InsuranceResponseDto(
                insurance.getDisplayName(), insurance.getDescription(),
                insurance.isRequiredAiAnalyses(),
                insurance.getFields()
        );
    }
}
