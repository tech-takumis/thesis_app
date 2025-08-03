package com.hashjosh.agripro.hpj_insurance.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.hashjosh.agripro.hpj_insurance.exception.InsuranceApplicationException;
import com.hashjosh.agripro.hpj_insurance.model.InsuranceField;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class DateValidator implements FieldValidatorStrategy{
    @Override
    public void validate(InsuranceField field, JsonNode value) {
        if (!value.isTextual() || !value.asText().matches("\\d{4}-\\d{2}-\\d{2}"))
            throw new InsuranceApplicationException(
                    "Field '" + field.getFieldName() + "'a valid ISO date (YYYY-MM-DD)",
                    "Field value error",
                    HttpStatus.BAD_REQUEST.value()
            );
    }
}
