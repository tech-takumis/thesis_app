package com.hashjosh.agripro.insurance.dto;

import com.hashjosh.agripro.insurance.enums.Datatype;

public record InsuranceFieldRequestDto(
        String keyName,
        Datatype fieldType,
        String displayName,
        String note,
        boolean is_required,
        Long insurance_type_id,

        // Optional request for data type equal to file onnly
        boolean hasCoordinate,
        String coordinate
) {
}
