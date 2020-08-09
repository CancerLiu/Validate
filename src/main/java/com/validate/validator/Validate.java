package com.validate.validator;

import com.validate.validator.handler.ValidatorHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
public @interface Validate {

    /**
     * 校验的类型
     */
    ValidateType type() default ValidateType.notNull;

    /**
     * String类型校验字符最长长度
     */
    int lengthMax() default Integer.MAX_VALUE;

    /**
     * String类型校验字符最短长度，默认0
     */
    int lengthMin() default 0;

    /**
     * 错误提示语
     */
    String message() default "";

    /**
     * 用户自定义逻辑
     */
    Class<? extends ValidatorHandler> customWay() default ValidatorHandler.class;
}
