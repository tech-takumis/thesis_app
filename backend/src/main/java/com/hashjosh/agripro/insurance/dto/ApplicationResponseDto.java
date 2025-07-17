package com.hashjosh.agripro.insurance.dto;

import com.hashjosh.agripro.insurance.models.InsuranceType;

import java.util.Map;

public record ApplicationResponseDto (
//        InsuranceResponseDto insurance,
        String status,
        Map<String, String> fieldValues
){
}
