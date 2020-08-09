package com.validate.demo;

import com.validate.validator.Validate;

public class Test {


    public void testOne() throws InstantiationException, IllegalAccessException {
        User user = new User().setName("liuchao").setCertificate("510106199007032916").setCellphone("13730885730");

//        ValidatorUtil.validator(user);
    }

    public void testTwo(@Validate String cellPhone,@Validate String certificate){

    }

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, NoSuchMethodException {
            Test test = new Test();
            test.testOne();
//        System.out.println( );
    }
}
