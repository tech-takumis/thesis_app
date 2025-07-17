package com.hashjosh.agripro.insurance.dto;

import java.util.Map;

public record InsuranceApplicationRequestDto(
    Map<String, String> fieldValues
) {
}
