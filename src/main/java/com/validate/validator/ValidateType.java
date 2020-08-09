package com.validate.validator;

import com.validate.validator.handler.*;

public enum ValidateType {
    //枚举类的一个对象对应一个解析器
    cellphone(new CellphoneHandler(), "手机号码格式不正确"),
    notNull(new NotNullHandler(), "格式不正确（为空或者字符长度不正确）"),
    certificate(new CertificateHandler(), "身份证号码格式不正确"),
    future(new FutureHandler(), "必须是一个将来的时间"),
    past(new PastHandler(), "必须是一个过去的时间"),
    custom(null, "用户自定义判定逻辑不通过");

    private ValidatorHandler handler;
    private String message;

    public ValidatorHandler getHandler() {
        return handler;
    }

    public ValidateType setHandler(ValidatorHandler handler) {
        this.handler = handler;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ValidateType setMessage(String message) {
        this.message = message;
        return this;
    }

    ValidateType(ValidatorHandler handler, String message) {
        this.handler = handler;
        this.message = message;
    }
}
