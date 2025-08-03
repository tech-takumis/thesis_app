package com.hashjosh.agripro.hpj_insurance.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.hashjosh.agripro.hpj_insurance.exception.InsuranceApplicationException;
import com.hashjosh.agripro.hpj_insurance.model.InsuranceField;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.stream.StreamSupport;

@Component
public class SelectValidator implements FieldValidatorStrategy {
    @Override
    public void validate(InsuranceField field, JsonNode value) {
        if (!value.isTextual()) {
            throw new InsuranceApplicationException(
                    "Field '" + field.getFieldName() + "' must be a text value (SELECT)",
                    "Field value error",
                    HttpStatus.BAD_REQUEST.value()
            );
        }

        JsonNode allowedChoices = field.getChoices().get(field.getFieldName());

        if (allowedChoices == null || !allowedChoices.isArray()) {
            throw new InsuranceApplicationException(
                    "Choices for field '" + field.getFieldName() + "' are not defined correctly.",
                    "Field choices missing or invalid",
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
        }

        String submitted = value.asText();
        boolean isValid = StreamSupport.stream(allowedChoices.spliterator(), false)
                .anyMatch(choice -> choice.asText().equalsIgnoreCase(submitted));

        if (!isValid) {
            throw new InsuranceApplicationException(
                    "Invalid value '" + submitted + "' for field '" + field.getFieldName() + "'. Allowed: " + allowedChoices,
                    "Invalid choice",
                    HttpStatus.BAD_REQUEST.value()
            );
        }
    }
}
