package com.hashjosh.agripro.insurance.dto;

import java.sql.Timestamp;
import java.util.Date;

public record InsuranceTypeResponseDto(
        Long id,
        String displayName,
        String description,
        boolean requiredAiAnalyses,
        Timestamp createAt,
        Timestamp updatedAt
) {
}
