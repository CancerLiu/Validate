package com.validate.validator;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 用于记录判断中常用的一些字段
 */
public class ValidatorUtil {

    public final static Integer CERTIFICATE_OLD_LENGTH = 15;

    public final static Integer CERTIFICATE_NEW_LENGTH = 18;

    public final static String[] IDENTIFI_CODE = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};

    public final static Integer[] COEFFICIENT_CODE = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    public final static Integer CERTIFICATE_DIVISOR = 11;

    private final static List<String> PRIMITIVES = Lists.newArrayList("java.lang.String", "java.lang.Integer", "java.lang.Double", "java.lang.Character", "java.lang.Byte", "java.lang.Short", "java.lang.Long", "java.lang.Float", "java.lang.Boolean", "java.util.Date");

    public static String defaultIfEmpty(final String str, final String defaultStr) {
        return "".equals(str) ? defaultStr : str;
    }

    public static Boolean isPrimitive(String typeString) {

        return PRIMITIVES.contains(typeString);
    }

    public static String getCertificateValidateBit(char[] charArray) {
        Integer total = 0;
        for (int i = 0; i < ValidatorUtil.COEFFICIENT_CODE.length; i++) {
            total += ValidatorUtil.COEFFICIENT_CODE[i] * (charArray[i] - 48);
        }
        Integer remainder = total % ValidatorUtil.CERTIFICATE_DIVISOR;
        return ValidatorUtil.IDENTIFI_CODE[remainder];
    }

}
