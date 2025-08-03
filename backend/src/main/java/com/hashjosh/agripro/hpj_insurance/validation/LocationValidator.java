package com.hashjosh.agripro.hpj_insurance.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.hashjosh.agripro.hpj_insurance.model.InsuranceField;
import org.springframework.stereotype.Component;

@Component
public class LocationValidator implements FieldValidatorStrategy{
    @Override
    public void validate(InsuranceField field, JsonNode value) {
        // need to implement location validation
    }
}
