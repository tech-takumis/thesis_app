package com.hashjosh.agripro.insurance.dto;

import com.hashjosh.agripro.insurance.models.InsuranceField;

import java.util.List;

public record InsuranceResponseDto(
        Long id,
        String displayName,
        String description,
        boolean requiredAiAnalyses,

        // Insurance Field
        List<InsuranceField> fields

) {
}
