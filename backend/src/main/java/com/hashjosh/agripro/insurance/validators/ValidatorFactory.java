package com.hashjosh.agripro.insurance.validators;

import com.hashjosh.agripro.exception.InvalidApplicationException;
import com.hashjosh.agripro.insurance.enums.Datatype;

import java.util.Map;

public class ValidatorFactory {
    private static final Map<Datatype, FieldValidatorStrategy> validatorMap = Map.of(
        Datatype.Text, new TextFieldValidator(),
            Datatype.Number, new NumberFieldValidator(),
            Datatype.Integer, new IntegerFieldValidator(),
            Datatype.Boolean, new BooleanFieldValidator(),
            Datatype.Timestamp, new TimestampFieldValidator(),
            Datatype.File, new FileFieldValidator()
    );

    public static FieldValidatorStrategy getValidator(Datatype datatype) {
        FieldValidatorStrategy validator = validatorMap.get(datatype);
        if(validator == null) {
            throw new InvalidApplicationException("Unsupported datatype: " + datatype);
        }
        return validator;
    }
}
