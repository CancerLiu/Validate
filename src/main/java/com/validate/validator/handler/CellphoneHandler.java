package com.validate.validator.handler;

import com.validate.validator.Validate;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;

import java.util.Locale;

/**
 * 收集号码的判定，具体使用了google的i18n工具包
 */
public class CellphoneHandler implements ValidatorHandler {


    private Boolean getDescriptionForNumber(String cellphone) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        PhoneNumberOfflineGeocoder phoneNumberOfflineGeocoder = PhoneNumberOfflineGeocoder.getInstance();

        try {
            String language = "CN";
            Phonenumber.PhoneNumber referencePhonenumber = phoneUtil.parse(cellphone, language);
            return !"".equals(phoneNumberOfflineGeocoder.getDescriptionForNumber(referencePhonenumber, Locale.CHINA));
        } catch (NumberParseException e) {
            return Boolean.FALSE;
        }
    }

    @Override
    public boolean validate(Object fieldInstance, Validate validate) {
        return getDescriptionForNumber((String) fieldInstance);
    }
}
