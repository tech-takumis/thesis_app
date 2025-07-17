package com.hashjosh.agripro.insurance.dto;

import java.util.List;

public record InsuranceRequestDto(

        // Insurance Type
        String displayName,
        String description,
        boolean requiredAiAnalyses,

        // Insurance Field
       List<InsuranceFieldRequestDto> fields
) {
}
