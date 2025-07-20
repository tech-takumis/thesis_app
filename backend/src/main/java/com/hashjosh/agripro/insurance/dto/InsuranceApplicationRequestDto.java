package com.hashjosh.agripro.insurance.dto;

import com.hashjosh.agripro.insurance.models.InsuranceField;

import java.util.List;

public record InsuranceApplicationRequestDto(
    List<InsuranceField> values
) {
}
