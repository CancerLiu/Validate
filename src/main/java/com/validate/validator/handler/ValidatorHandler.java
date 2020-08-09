package com.validate.validator.handler;

import com.validate.validator.Validate;
import org.apache.commons.lang3.StringUtils;

/**
 * 判定中最基本的几个方法，以默认方法写在接口中。这几个方法不管在啥类型中都会用到。
 */
public interface ValidatorHandler {

    /**
     * 子类中需要实现的判定逻辑，不同的判定类型和对象需要不同的判定类型
     */
    boolean validate(Object fieldInstance, Validate validate);

    /**
     * 进行非空判定
     */
    default boolean notNull(Object fieldInstance, Validate validate) {
        return StringUtils.isNotBlank(String.valueOf(fieldInstance)) && StringUtils.isNotEmpty(String.valueOf(fieldInstance));
    }

    /**
     * 进行字段长度最长判定
     */
    default boolean checkLengthMax(Object fieldInstance, Validate validate) {
        return (String.valueOf(fieldInstance)).length() <= validate.lengthMax();
    }

    /**
     * 进行字段长度最短判定
     */
    default boolean checkLengthMin(Object fieldInstance, Validate validate) {
        return (String.valueOf(fieldInstance)).length() >= validate.lengthMin();
    }
}
