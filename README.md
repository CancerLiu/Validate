# Validate
自定义的一个简单的前端传入参数校验模块，相较于Hibernate提供的校验模块更加轻便。同时提供了扩展接口

## 1 功能与引入
### 1.1 功能
  主要用于基于SpringMVC的前后端分离项目，前端传入参数时，对常见参数的校验。目前有对手机号码(国内)、身份证号码、字符串、时间(相当于当前时间)等的校验。同时提供了扩展自定义校验逻辑的接口。相较于Hibernate提供的相关参数校验功能，更加的轻量级。
  
  目前可以借由SpringBoot starter引入使用和常规Spring项目引入使用，具体见后。
### 1.2 引入
#### 1.2.1 SpringBoot starter引入
  因为已经封装了springBoot starter的功能，所以项目下载后，-Dmaven.test.skip=true。然后在pom文件中加入如下dependency
  ```
        <dependency>
            <groupId>com.validator</groupId>
            <artifactId>parameter-checking-spring-boot-starter</artifactId>
            <version>1.0.0${project.validate}</version>
        </dependency>
  ```
  
  pom文件中的其他如springBoot之类的的基本dependency这里不再赘述。后同。
  
#### 1.2.2 普通spring项目引入
  相较于starter方式引入稍微麻烦点。还是先通过pom文件引入依赖
   ```
        <dependency>
            <groupId>com.validator</groupId>
            <artifactId>parameter-checking-spring-boot-starter</artifactId>
            <version>1.0.0${project.validate}</version>
        </dependency>
  ```
  
  然后在springMVC-mvc.xml配置文件中，写入如下配置代码
  ```
        <bean id="validateAdvisor" class="com.validate.validator.ValidatorAdvisor"/>

        <aop:config proxy-target-class="true">
            <aop:aspect ref="validateAdvisor">
                <aop:pointcut expression="@annotation(com.validate.validator.Validate)" id="validateCut"/>
                <aop:around method="validateHandler" pointcut-ref="validateCut"/>
            </aop:aspect>
        </aop:config>
  ```
  
  ## 2 代码中使用
  ### 2.1 普通使用
  引入后可以直接在代码中使用，主要是围绕两个注解来使用的@Validate和@Cascading  
   **2.1.1 @Validate注解**  
  既可以标注在Controller方法上，也可以标注在具体的方法参数上。标注在Controller方法上是作为一个标识注解来使用的，只有被@Validate注解了的方法和该方法中被@Validate或@Cascading注解了的参数会被进行参数校验  
  
  **type**  
  默认为String的非空判定。其他的现阶段功能能够对国内手机号、身份证号、时间进行和字符串简单校验。具体通过@Validate的type属性来指定，依次为ValidateType.cellphone、ValidateType.certificate、ValidateType.past和ValidateType.future。 
  
  **lengthMax和lengthMin**  
  用于指定被校验字段的字符长度范围。一般来说只针对Sting才有意义  
  
  **message**  
  用于指定校验不通过时返回给前端的提示信息。只会返回第一个校验不通过的message。如果不指定则会使用默认的message。此处的报错如果要集成为统一格式返回，则可以在CustomException中修改代码。  
  
  **customWay**  
  用于自定义返回逻辑时的属性，一般不使用。具体使用时，该属性赋予用于自定义的类的class对象(一般会使用静态内部类)，该类需要实现ValidatorHandler接口，然后重写其中的validate方法，通过返回的boolean值来判断该属性是否判定通过。  
  其中validate方法的参数列表为(Object fieldInstance, Validate validate),前者就是当前@Validate修饰的属性，后者是当前修饰属性的@Validate本身，可以根据这个注解对象获取到注解的相关属性。  
  具体使用见后面示例。
  
  **2.1.2 @Cascading**  
  该注解主要是为了考虑到级联判断的情况。如果SpringMVC的Controller中方法的参数是一个对象，而你想判断该方法中的一些字段，则可以在该对象参数前标注@Cascading注解，然后在该对象中需要校验的成员变量上进一步标注@Validate。或者更进一步的标注@Cascading。@Cascading没有其他属性，只是一个标识注解。
  
  ### 2.2 示例  
  以一个典型的Controller层方法为例，这里入参的user被@Cascading注解，说明其需要进行内部成员变量的相关参数校验，具体看第二段User代码。而userDetails就是普通的String类型校验，使用message属性写明具体校验不通过的提示信息
  ```
    @GetMapping(value = "validate/test")
    @Validate
    public void testValidate(@Cascading User user ,@Validate(message = "用户详情描述为空") String userDetails){
        logger.info("Validate的测试");
    }
  ```
  
  如下是User内部对于需要校验的成员变量的操作。其中使用静态内部类来承载具体的自定义校验逻辑
  ```
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

    /**
     * 自定义逻辑类。根据返回的
     */
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
  
  ```
 
  
