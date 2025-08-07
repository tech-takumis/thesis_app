package com.hashjosh.agripro.insurance.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.hashjosh.agripro.insurance.dto.ValidationError;
import com.hashjosh.agripro.insurance.model.InsuranceField;

import java.util.List;

public interface FieldValidatorStrategy {
    List<ValidationError> validate(InsuranceField field, JsonNode value);

}
