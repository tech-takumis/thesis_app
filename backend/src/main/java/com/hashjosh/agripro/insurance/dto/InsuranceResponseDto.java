package com.hashjosh.agripro.insurance.dto;

import java.util.List;

public record InsuranceResponseDto(
        Long id,
        String name,
        String layout,
        String description,
        boolean requiredAIAnalysis,
        List<InsuranceSectionResponseDto> sections
) {
}
