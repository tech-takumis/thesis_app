package com.hashjosh.agripro.insurance.dto;

import com.hashjosh.agripro.insurance.enums.Datatype;

public record InsuranceFieldRequestDto(
        String keyName,
        Datatype fieldType,
        String displayName,
//        FormField ui_input_type,
        String note,
        boolean is_required,
        Long insurance_type_id
) {
}
