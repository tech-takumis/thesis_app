package com.hashjosh.agripro.hpj_insurance.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.hashjosh.agripro.hpj_insurance.dto.InsuranceApplicationRequest;
import com.hashjosh.agripro.hpj_insurance.dto.InsuranceRequestDto;
import com.hashjosh.agripro.hpj_insurance.dto.InsuranceFieldDto;
import com.hashjosh.agripro.hpj_insurance.dto.InsuranceResponseDto;
import com.hashjosh.agripro.hpj_insurance.enums.StatusType;
import com.hashjosh.agripro.hpj_insurance.model.InsuranceApplication;
import com.hashjosh.agripro.hpj_insurance.model.InsuranceField;
import com.hashjosh.agripro.hpj_insurance.model.InsuranceType;
import com.hashjosh.agripro.user.models.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InsuranceMapper {
    public InsuranceType toInsuranceType(InsuranceRequestDto dto) {
        return InsuranceType.builder()
                .InsuranceType(dto.type())
                .description(dto.description())
                .requiredAIAnalysis(dto.requiredAIAnalysis())
                .build();
    }

    public List<InsuranceField> toInsuranceField(InsuranceRequestDto dto, InsuranceType insuranceType) {
        return dto.fields().stream().map(
                field -> InsuranceField.builder()
                        .insuranceType(insuranceType)
                        .fieldName(field.fieldName())
                        .fieldType(field.fieldType())
                        .defaultValue(field.defaultValue())
                        .label(field.label())
                        .choices(field.choices())
                        .required(field.required())
                        .validationRegex(field.validationRegex())
                        .build()
        ).collect(Collectors.toList());
    }

    public InsuranceFieldDto toInsuranceFieldResponse(InsuranceField field){
        return  new InsuranceFieldDto(
                field.getFieldName(),
                field.getLabel(),
                field.getFieldType(),
                field.getChoices(),
                field.isRequired(),
                field.getDefaultValue(),
                field.getValidationRegex()
        );
    }

    public InsuranceResponseDto toInsuranceResponse(InsuranceType insuranceType) {
        return  new InsuranceResponseDto(
                insuranceType.getId(),
                insuranceType.getInsuranceType(),
                insuranceType.getDescription(),
                insuranceType.getRequiredAIAnalysis(),
                insuranceType.getInsuranceField().stream()
                        .map(this::toInsuranceFieldResponse).collect(Collectors.toList())
        );
    }

    public InsuranceApplication toInsuranceApplication(JsonNode values,
                                                       InsuranceType type, User user) {
        return InsuranceApplication.builder()
                .user(user)
                .insuranceType(type)
                .status(StatusType.PENDING.name())
                .dynamicFields(values)
                .build();
    }
}
