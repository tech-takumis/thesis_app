package com.hashjosh.agripro.hpj_insurance.validation;

import com.hashjosh.agripro.hpj_insurance.enums.FieldType;
import com.hashjosh.agripro.hpj_insurance.exception.InsuranceApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class FieldValidatorFactory {

    private final Map<FieldType, FieldValidatorStrategy> strategies = new HashMap<>();

    @Autowired
    public FieldValidatorFactory(
            BooleanValidator booleanValidator,
            DateValidator dateValidator,
            FileValidator fileValidator,
            LocationValidator locationValidator,
            NumberValidator numberValidator,
            SelectValidator selectValidator,
            SignatureValidator signatureValidator,
            TextValidator textValidator,
            MultiSelectValidator multiSelectValidator
    ) {
        strategies.put(FieldType.BOOLEAN, booleanValidator);
        strategies.put(FieldType.DATE, dateValidator);
        strategies.put(FieldType.FILE, fileValidator);
        strategies.put(FieldType.LOCATION, locationValidator);
        strategies.put(FieldType.NUMBER, numberValidator);
        strategies.put(FieldType.SELECT, selectValidator);
        strategies.put(FieldType.SIGNATURE, signatureValidator);
        strategies.put(FieldType.TEXT, textValidator);
        strategies.put(FieldType.MULTI_SELECT, multiSelectValidator);
    }

    public FieldValidatorStrategy getStrategy(FieldType fieldType) {
        FieldValidatorStrategy strategy = strategies.get(fieldType);
        if (strategy == null) {
            throw new InsuranceApplicationException(
                    "Unsupported data type " + fieldType,
                    "Unsupported data type. Contact admin.",
                    HttpStatus.BAD_REQUEST.value()
            );
        }
        return strategy;
    }
}
