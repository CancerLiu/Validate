package com.validate.validator.handler;

import com.validate.validator.Validate;
import com.validate.validator.ValidatorUtil;

/**
 * 身份证号码判定。这里需要一定的身份证号码知识
 */
public class CertificateHandler implements ValidatorHandler {


    private Boolean checkCertificate(String certificate) {
        //现在就只校验18位的身份证号码，15位的会直接返回false
        char[] charArray = certificate.toCharArray();
        if (ValidatorUtil.CERTIFICATE_NEW_LENGTH == charArray.length) {

            String lastStr = ValidatorUtil.getCertificateValidateBit(charArray);

            return lastStr.equals("" + charArray[ValidatorUtil.CERTIFICATE_NEW_LENGTH - 1]);
        } else {
            if (ValidatorUtil.CERTIFICATE_OLD_LENGTH == charArray.length) {
                //如果为15位身份证，则将之转换为18位身份证之后再用此方法进行校验（15位身份证暂时不能进行校验）
                StringBuilder sb = new StringBuilder(certificate);
                sb.insert(6, "19");
                sb.append(ValidatorUtil.getCertificateValidateBit(charArray));
                this.checkCertificate(sb.toString());
            }
        }
        return Boolean.FALSE;
    }
    @Override
    public boolean validate(Object fieldInstance, Validate validate) {
        return checkCertificate((String) fieldInstance);
    }
}
