package com.hashjosh.agripro.insurance.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.hashjosh.agripro.insurance.dto.ValidationError;
import com.hashjosh.agripro.insurance.model.InsuranceField;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MultiSelectValidator implements FieldValidatorStrategy{
    @Override
    public List<ValidationError> validate(InsuranceField field, JsonNode value) {
        List<ValidationError> errors = new ArrayList<>();
        if (!value.isArray()){
            errors.add(new ValidationError(
                    field.getFieldName(),
                    "Field must be an array of text values (MULTI_SELECT)"
            ));
        }

        return errors;
    }
}
