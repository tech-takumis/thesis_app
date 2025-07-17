package com.hashjosh.agripro.insurance.dto;

import com.hashjosh.agripro.insurance.enums.Datatype;

public record InsuranceFieldRequestDto(
        Datatype fieldType,
        String displayName,
        String note,
        boolean is_required,

        // Optional request for data type equal to file onnly
       boolean hasCoordinate
) {
}
