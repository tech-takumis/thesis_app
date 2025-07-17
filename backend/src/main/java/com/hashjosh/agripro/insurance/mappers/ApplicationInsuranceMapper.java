package com.hashjosh.agripro.insurance.mappers;

import com.github.slugify.Slugify;
import com.hashjosh.agripro.insurance.dto.ApplicationResponseDto;
import com.hashjosh.agripro.insurance.dto.InsuranceApplicationRequestDto;
import com.hashjosh.agripro.insurance.dto.InsuranceRequestDto;
import com.hashjosh.agripro.insurance.dto.InsuranceResponseDto;
import com.hashjosh.agripro.insurance.enums.Status;
import com.hashjosh.agripro.insurance.models.InsuranceApplication;
import com.hashjosh.agripro.insurance.models.InsuranceField;
import com.hashjosh.agripro.insurance.models.InsuranceType;
import com.hashjosh.agripro.user.models.User;
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

    public List<InsuranceField> requestToInsuranceFields(InsuranceRequestDto dto,
                                                         InsuranceType insuranceType) {

        Slugify slugify;
        return dto.fields().stream().map(
                field -> InsuranceField.builder()
                        .key(toSlug(field.displayName()))
                        .displayName(field.displayName())
                        .fieldType(field.fieldType())
                        .note(field.note())
                        .is_required(field.is_required())
                        .insuranceType(insuranceType)
                        .hasCoordinate(field.hasCoordinate())
                        .build()
        ).collect(Collectors.toList());
    }

    public InsuranceApplication toInsuranceApplication(InsuranceApplicationRequestDto dto,
                                                       InsuranceType  insuranceType,
                                                       User user) {
        return InsuranceApplication.builder()
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .status(Status.PENDING)
                .user(user)
                .insuranceType(insuranceType)
                .fieldValues(dto.fieldValues())
                .build();
    }

    public InsuranceResponseDto toInsuranceTypeResponse(InsuranceType insurance) {
        return new InsuranceResponseDto(
                insurance.getDisplayName(), insurance.getDescription(),
                insurance.isRequiredAiAnalyses(),
                insurance.getFields()
        );
    }


    public ApplicationResponseDto toApplicationResponse(InsuranceApplication application) {
        return new ApplicationResponseDto(
//                toInsuranceTypeResponse(application.getInsuranceType()),
                application.getStatus().name(),
                application.getFieldValues()
        );
    }
    private String toSlug(String input) {
        return input.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .trim()
                .replaceAll("\\s+", "-");
    }

}
