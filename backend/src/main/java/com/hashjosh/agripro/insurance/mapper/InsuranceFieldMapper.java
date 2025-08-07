package com.hashjosh.agripro.insurance.mapper;

import com.hashjosh.agripro.insurance.dto.InsuranceFieldDto;
import com.hashjosh.agripro.insurance.dto.InsuranceFieldResponseDto;
import com.hashjosh.agripro.insurance.model.InsuranceField;
import com.hashjosh.agripro.insurance.model.InsuranceSection;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InsuranceFieldMapper {


    public InsuranceFieldResponseDto toInsuranceFieldResponse(InsuranceField field){
        return  new InsuranceFieldResponseDto(
                field.getId(),
                field.getKey(),
                field.getFieldName(),
                field.getLabel(),
                field.getFieldType(),
                field.getChoices(),
                field.isRequired(),
                field.getDefaultValue(),
                field.getValidationRegex()
        );
    }

    public List<InsuranceField> toInsuranceFieldList(List<InsuranceFieldDto> fields) {
        return fields.stream().map(
            field -> InsuranceField.builder()
                    .key(field.key())
                    .fieldName(field.fieldName())
                    .label(field.label())
                    .fieldType(field.fieldType())
                    .choices(field.choices())
                    .required(field.required())
                    .defaultValue(field.defaultValue())
                    .validationRegex(field.validationRegex())
                .build()
        ).collect(Collectors.toList());
    }

    public InsuranceField toInsuranceField(InsuranceFieldDto dto, InsuranceSection section) {
        return InsuranceField.builder()
                .insuranceSection(section)
                .key(dto.key())
                .fieldName(dto.fieldName())
                .label(dto.label())
                .fieldType(dto.fieldType())
                .choices(dto.choices())
                .required(dto.required())
                .defaultValue(dto.defaultValue())
                .validationRegex(dto.validationRegex())
                .build();
    }
}
