package com.validate.validator.handler;

import com.validate.validator.Validate;
import com.validate.validator.utils.MyDateUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

/**
 * 关于时间的判定————是否在过去
 */
public class PastHandler implements ValidatorHandler {

    @Override
    public boolean validate(Object fieldInstance, Validate validate) {
        if (fieldInstance instanceof Date) {
            Date date = DateUtils.addSeconds(MyDateUtils.getDay(new Date(), 1), -1);
            return ((Date) fieldInstance).before(date);
        }
        return false;
    }
}
