package com.hashjosh.agripro.insurance.dto;

import java.util.List;

public record InsuranceSectionRequestDto(
        String title,
        List<InsuranceFieldDto> fields
) {
}
