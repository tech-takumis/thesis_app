package com.hashjosh.agripro.insurance.dto;

public record InsuranceTypeRequestDto(
        String displayName,
        String description,
        boolean requiredAiAnalyses
) {
}
