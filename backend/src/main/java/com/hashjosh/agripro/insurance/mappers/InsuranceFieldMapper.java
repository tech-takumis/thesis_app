package com.hashjosh.agripro.insurance.mappers;

import com.hashjosh.agripro.insurance.dto.InsuranceFieldRequestDto;
import com.hashjosh.agripro.insurance.dto.InsuranceFieldResponseDto;
import com.hashjosh.agripro.insurance.models.InsuranceField;
import com.hashjosh.agripro.insurance.models.InsuranceType;
import org.springframework.stereotype.Service;

@Service
public class InsuranceFieldMapper {
    public InsuranceField toInsuranceField(InsuranceFieldRequestDto field) {
        return InsuranceField.builder()
                .keyName(field.keyName())
                .fieldType(field.fieldType())
                .displayName(field.displayName())
                .note(field.note())
                .is_required(field.is_required())
                .insuranceType(
                        InsuranceType.builder()
                                .id(field.insurance_type_id())
                                .build()
                )
                .build();
    }

    public InsuranceFieldResponseDto toInsuranceFieldResponse(InsuranceField save) {
        return new InsuranceFieldResponseDto(
                save.getKeyName(),save.getFieldType(),save.getDisplayName(),
                save.getNote(),save.is_required(),
                save.getInsuranceType().getId()
        );
    }
}
