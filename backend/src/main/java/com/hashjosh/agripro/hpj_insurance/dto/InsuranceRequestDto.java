package com.hashjosh.agripro.hpj_insurance.dto;

import java.util.List;

public record InsuranceRequestDto(
        String type,
        String description,
        boolean requiredAIAnalysis,
        List<InsuranceFieldDto> fields
) {
}
