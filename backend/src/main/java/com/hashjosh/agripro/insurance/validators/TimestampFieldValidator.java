package com.hashjosh.agripro.insurance.validators;

import com.hashjosh.agripro.exception.InvalidApplicationException;

import java.time.Instant;
import java.time.format.DateTimeParseException;

public class TimestampFieldValidator implements FieldValidatorStrategy {
    @Override
    public void validate(String value) {
        try {
            Instant.parse(value); // assumes ISO8601
        } catch (DateTimeParseException e) {
            throw new InvalidApplicationException("Invalid timestamp format (must be ISO8601)");
        }
    }
}

