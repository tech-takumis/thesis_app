package com.hashjosh.agripro.insurance.validators;

import com.hashjosh.agripro.exception.InvalidApplicationException;
import com.hashjosh.agripro.insurance.models.InsuranceField;

public class FileFieldValidator implements FieldValidatorStrategy {
    @Override
    public void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidApplicationException("File must be uploaded");
        }

        // optionally: check for known file path format or path prefix
        if (!value.startsWith("/uploads/")) {
            throw new InvalidApplicationException("Invalid file path reference");
        }
    }
}
