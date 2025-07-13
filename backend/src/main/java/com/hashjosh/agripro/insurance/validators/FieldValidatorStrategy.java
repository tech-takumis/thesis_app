package com.hashjosh.agripro.insurance.validators;

import com.hashjosh.agripro.insurance.models.InsuranceField;

public interface FieldValidatorStrategy {
    void validate(String fieldName, String fieldValue, InsuranceField field);
}
