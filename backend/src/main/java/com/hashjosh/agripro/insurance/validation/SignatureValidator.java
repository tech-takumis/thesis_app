package com.hashjosh.agripro.insurance.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.hashjosh.agripro.insurance.dto.ValidationError;
import com.hashjosh.agripro.insurance.model.InsuranceField;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SignatureValidator implements FieldValidatorStrategy{
    @Override
    public List<ValidationError> validate(InsuranceField field, JsonNode value) {
        // Need to implement signature validation
        return null;
    }
}
