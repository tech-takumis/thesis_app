package com.hashjosh.agripro.hpj_insurance.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.hashjosh.agripro.hpj_insurance.model.InsuranceField;
import org.springframework.stereotype.Component;

@Component
public class SignatureValidator implements FieldValidatorStrategy{
    @Override
    public void validate(InsuranceField field, JsonNode value) {
        // Need to implement signature validation
    }
}
