package com.validate.validator;

import java.lang.annotation.Annotation;

/**
 * 用于封装方法中的参数相关信息
 */
public class Param {
    /**
     * 参数类型名字
     */
    private String simpleTypeName;
    /**
     * 参数名
     */
    private String paramName;
    /**
     * 参数的类型
     */
    private Class<?> type;
    /**
     * 参数的值
     */
    private Object value;
    /**
     * 参数的注解
     */
    private Annotation annotation;

    public String getSimpleTypeName() {
        return simpleTypeName;
    }

    public Param setSimpleTypeName(String simpleTypeName) {
        this.simpleTypeName = simpleTypeName;
        return this;
    }

    public String getParamName() {
        return paramName;
    }

    public Param setParamName(String paramName) {
        this.paramName = paramName;
        return this;
    }

    public Class<?> getType() {
        return type;
    }

    public Param setType(Class<?> type) {
        this.type = type;
        return this;
    }

    public Object getValue() {
        return value;
    }

    public Param setValue(Object value) {
        this.value = value;
        return this;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public Param setAnnotation(Annotation annotation) {
        this.annotation = annotation;
        return this;
    }
}
