package com.hashjosh.agripro.hpj_insurance.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.hashjosh.agripro.hpj_insurance.enums.FieldType;

public record InsuranceFieldDto(
        String fieldName,
        String label,
        FieldType fieldType,
        JsonNode choices,
        Boolean required,
        String defaultValue,
        String validationRegex) {
}
