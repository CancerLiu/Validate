package com.validate.validator.handler;

import com.validate.validator.Validate;

public class NotNullHandler implements ValidatorHandler {


    @Override
    public boolean validate(Object fieldInstance, Validate validate) {
        return notNull(fieldInstance, validate) && checkLengthMax(fieldInstance, validate) && checkLengthMin(fieldInstance, validate);
    }
}
