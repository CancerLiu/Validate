package com.validate.demo;

import com.validate.validator.Validate;
import com.validate.validator.ValidateType;
import com.validate.validator.handler.ValidatorHandler;

public class User {

    @Validate(type = ValidateType.certificate)
    private String certificate;

    //@Validate(length = 5,fieldName = "姓名")
    //需要先将type声明为custom即用户自定义状态，然后再通过customWay标签传入自定义的判定逻辑所属类的Class对象;
    //其中默认传入的HandlerParam中有当前属性的Field，当前User和当前注解三个属性对象，用户自定义逻辑的时候可使用
    @Validate(type = ValidateType.custom, customWay = customFirst.class)
    private String name;

    @Validate(type = ValidateType.cellphone, lengthMax = 11)
    private String cellphone;


    public static class customFirst implements ValidatorHandler {
        @Override
        public boolean validate(Object fieldInstance, Validate validate) {
            return Boolean.FALSE;
        }
    }


    public String getCertificate() {
        return certificate;
    }

    public User setCertificate(String certificate) {
        this.certificate = certificate;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getCellphone() {
        return cellphone;
    }

    public User setCellphone(String cellphone) {
        this.cellphone = cellphone;
        return this;
    }
}
