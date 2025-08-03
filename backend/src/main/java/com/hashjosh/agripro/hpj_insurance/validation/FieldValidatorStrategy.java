package com.hashjosh.agripro.hpj_insurance.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.hashjosh.agripro.hpj_insurance.model.InsuranceField;

public interface FieldValidatorStrategy {
    void validate(InsuranceField field, JsonNode value);

}
