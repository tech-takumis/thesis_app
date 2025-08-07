package com.hashjosh.agripro.insurance.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.hashjosh.agripro.insurance.dto.ValidationError;
import com.hashjosh.agripro.insurance.model.InsuranceField;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class SelectValidator implements FieldValidatorStrategy {
    @Override
    public List<ValidationError> validate(InsuranceField field, JsonNode value) {
        List<ValidationError> errors = new ArrayList<>();
        if (!value.isTextual()) {
           errors.add(new ValidationError(
                   field.getFieldName(),
                   "Field must be a text value (SELECT)"
           ));
        }

        JsonNode allowedChoices = field.getChoices().get(field.getFieldName());

        if (allowedChoices == null || !allowedChoices.isArray()) {
            errors.add(new ValidationError(
                    field.getFieldName(),
                    "Field must have a valid set of choices (SELECT)"
            ));
        }

        String submitted = value.asText();
        boolean isValid = StreamSupport.stream(allowedChoices.spliterator(), false)
                .anyMatch(choice -> choice.asText().equalsIgnoreCase(submitted));

        if (!isValid) {
           errors.add(new ValidationError(
                   field.getFieldName(),
                   "Invalid value '" + submitted + "' for field '" + field.getFieldName() + "'. Allowed: " + allowedChoices
           ));
        }
        return errors;
    }
}
