package com.validate.utils.exception;

public class CustomException extends RuntimeException {
    private static final long serialVersionUID = 6655164451288057079L;
    private Integer code = -1;

    public CustomException() {
    }

    public CustomException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public CustomException(String msg) {
        super(msg);
    }

    public CustomException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public CustomException(Throwable cause) {
        super(cause);
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}