package com.hashjosh.agripro.insurance.dto;

public record ValidationError(
        String fieldName,
        String message
) {
}
