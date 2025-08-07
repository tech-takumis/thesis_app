package com.hashjosh.agripro.insurance.dto;

import java.util.List;

public record InsuranceSectionResponseDto(
        Long id,
        String title,
        List<InsuranceFieldResponseDto> fields
) {
}
