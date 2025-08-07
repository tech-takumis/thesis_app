package com.hashjosh.agripro.insurance.dto;

import java.util.List;

public record InsuranceRequestDto(
        String name,
        String layout,
        String description,
        boolean requiredAIAnalysis,
        List<InsuranceSectionRequestDto> sections
) {
}
