package com.hashjosh.agripro.insurance.validators;

import com.hashjosh.agripro.exception.InvalidApplicationException;
import com.hashjosh.agripro.insurance.models.InsuranceField;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TimestampFieldValidator implements FieldValidatorStrategy
{
    private  static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    @Override
    public void validate(String fieldName, String fieldValue, InsuranceField field) throws InvalidApplicationException
    {
        try{
            LocalDateTime.parse(fieldValue, TIMESTAMP_FORMAT);
        }catch (DateTimeParseException e){
            throw new InvalidApplicationException("Field '" + fieldName + "' must be a valid timestamp in yyyy-MM-dd'T'HH:mm:ss format");
        }
    }
}
