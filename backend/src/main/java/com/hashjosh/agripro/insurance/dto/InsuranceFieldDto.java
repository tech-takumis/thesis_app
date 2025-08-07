package com.hashjosh.agripro.insurance.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.hashjosh.agripro.insurance.enums.FieldType;

public record InsuranceFieldDto(
        String key,
        String fieldName,
        String label,
        FieldType fieldType,
        JsonNode choices,
        Boolean required,
        String defaultValue,
        String validationRegex) {
}
