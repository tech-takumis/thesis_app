package com.hashjosh.agripro.insurance.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.hashjosh.agripro.insurance.dto.ValidationError;
import com.hashjosh.agripro.insurance.model.InsuranceField;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DateValidator implements FieldValidatorStrategy{
    @Override
    public List<ValidationError> validate(InsuranceField field, JsonNode value) {
        List<ValidationError> errors = new ArrayList<>();
        if (!value.isTextual() || !value.asText().matches("\\d{4}-\\d{2}-\\d{2}")){
            errors.add(new ValidationError(
                    field.getFieldName(),
                    "Field must be a valid ISO date (YYYY-MM-DD)"
            ));
        }
        return errors;
    }
}
