package com.validate.validator;

public class ValidatorResult {

    private Boolean validateResult;

    private String message;

    public Boolean getValidateResult() {
        return validateResult;
    }

    public ValidatorResult setValidateResult(Boolean validateResult) {
        this.validateResult = validateResult;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ValidatorResult setMessage(String message) {
        this.message = message;
        return this;
    }
}
