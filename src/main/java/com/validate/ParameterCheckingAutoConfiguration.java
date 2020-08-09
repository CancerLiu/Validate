package com.validate;

import com.validate.utils.common.AssertUtils;
import com.validate.utils.exception.CustomException;
import com.validate.utils.mapper.JsonMapper;
import com.validate.validator.*;
import com.validate.validator.handler.ValidatorHandler;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.PriorityOrdered;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


@Aspect
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
@Configuration

/**
 * 默认开启
 */
@ConditionalOnProperty(prefix = "validate", name = "enable",
        havingValue = "true", matchIfMissing = true)
public class ParameterCheckingAutoConfiguration extends HandlerInterceptorAdapter implements PriorityOrdered {

    private final static Logger logger = LoggerFactory.getLogger(ValidatorAdvisor.class);


    /**
     * 可知是环绕通知。通过标注的@Validator注解进入这里
     *
     * @param point 切点
     * @throws Throwable
     */
    @Around("@annotation(com.validate.validator.Validate)")
    public void validateHandler(ProceedingJoinPoint point) throws Throwable {
        //获得方法传入的参数对象
        Object[] objs = point.getArgs();
        logger.info("传入的值为:{}",JsonMapper.buildNonNullMapper().toJson(objs));

        //获得被代理的方法(得到方法签名)
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();

        //筛选出形参中所有的参数对象
        List<Param> params = assembleParam(method, objs);
        logger.info("方法参数列表为:{}",JsonMapper.buildNonNullMapper().toJson(params));

        //最终判定，如果有错误将错误给前端
        ValidatorResult validatorResult = validatorHandler(params);
        logger.info("最终判断的结果为:{}",JsonMapper.buildNonNullMapper().toJson(validatorResult));
        AssertUtils.isTrue(validatorResult.getValidateResult(), new CustomException(validatorResult.getMessage()));

        //环绕通知需要做的一步
        point.proceed();
    }


    /**
     * 组装方法的参数对象集合
     *
     * @param method 方法对象
     * @param objs   方法参数值对象
     * @return 方法参数集合
     */
    private List<Param> assembleParam(Method method, Object[] objs) throws IllegalAccessException {
        //得到该方法对象形参列表所有注解(是一个二维数组，两个维度分别对应了具体方法的第几个参数与具体参数的第几个注解)
        Annotation[][] annos = method.getParameterAnnotations();
        //得到该方法的形参列表中形参的类型
        Class<?>[] paramTypes = method.getParameterTypes();
        //得到该方法的形参列表中的形参对象
        Parameter[] parameters = method.getParameters();

        //需要Validator的参数对象
        List<Param> params = new ArrayList<>();
        for (int i = 0; i < annos.length; i++) {
            //这里循环取方法对象中第i个参数的第j个注解来判断
            for (int j = 0; j < annos[i].length; j++) {
                //如果出现指定的注解类型
                if (annos[i][j].annotationType() == Validate.class) {
                    //依次向参数对象中放入注解对象、参数名、参数简单类型名、参数类型和参数值
                    Param param = new Param().setAnnotation(annos[i][j])
                            .setParamName(parameters[i].getName())
                            .setSimpleTypeName(paramTypes[i].getSimpleName())
                            .setType(paramTypes[i])
                            .setValue(objs[i]);
                    params.add(param);
                } else if (annos[i][j].annotationType() == Cascading.class) {
                    /*@Cascding用于标注需要级联判断的情况(即参数是一个对象，对象中的字段需要进一步判断)，这里isPrimitive()用于判断
                      参数对象是否是原始类型*/
                    if (paramTypes[i].isPrimitive() || ValidatorUtil.isPrimitive(paramTypes[i].getName())) {
                        throw new CustomException("注解@Cascading使用错误");
                    }
                    checkObject(objs[i], params);
                }
            }
        }
        return params;
    }

    /**
     * 嵌套将对象中所有的待校验{@link Validate}的基本类型和String类型的字段和属性组装为Param对象,然后放入Param对象集合中;
     *
     * @param object 被检查的嵌套对象
     * @param params Param对象集合
     * @throws IllegalAccessException
     */
    private void checkObject(Object object, List<Param> params) throws IllegalAccessException {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            //得到相关字段的注解，并判断是否是Validate注解
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Validate) {
                    Param param = new Param().setAnnotation(annotation)
                            .setParamName(field.getName())
                            .setSimpleTypeName(field.getType().getSimpleName())
                            .setType(field.getType())
                            .setValue(field.get(object));
                    params.add(param);
                } else if (annotation instanceof Cascading) {
                    logger.info(field.getName());
                    logger.info(JsonMapper.buildNonNullMapper().toJson(field.get(object)));
                    //递归
                    checkObject(field.get(object), params);
                }
            }
        }
    }

    /**
     * 校验核心处理方法
     * 依次判定所有的Param对象，组装结果集并返回第一个验证不通过的结果集对象
     *
     * @param params 待验证的Param结果集合
     * @return 第一个校验不通过的属性的结果集（如果全部Param通过则返回最后一个验证的属性的结果集）;
     */
    private ValidatorResult validatorHandler(List<Param> params) throws IllegalAccessException, InstantiationException {
        //判定结果
        boolean flag = true;
        String message = "";
        for (Param param : params) {
            Validate validate = (Validate) param.getAnnotation();

            //具体的被标注了相关注解的参数所接收到的值
            Object fieldInstance = param.getValue();
            //得到校验的类型
            ValidateType validateType = validate.type();
            //进一步得到该类型的校验逻辑
            ValidatorHandler validatorHandler = validateType.getHandler();

            if (null != validatorHandler) {
                //此处使用服务自定义的判定逻辑进行判定
                logger.info("依次判断的值为:{}",fieldInstance);
                flag = validatorHandler.validate(fieldInstance, validate);
                if (!flag) {
                    //组装错误通知消息(注解中有用户自定义message就使用，否则使用服务默认的)并马上返回值
                    logger.debug("字段" + "   " + param.getParamName() + "   " + ValidatorUtil.defaultIfEmpty(validate.message(), validateType.getMessage()) + "  " + flag);
                    message = ValidatorUtil.defaultIfEmpty(validate.message(), validateType.getMessage());
                    return new ValidatorResult().setValidateResult(flag).setMessage(message);
                }
            } else {
                //用户自定义的判定条件
                //用户具体操作如下
                // ①需要将@Validate的type属性定义为ValidateType.custom
                // ②JavaBean中定义public和static修饰的内部类A，且实现ValidatorHandler，在重写的validate方法中重写自己的判定逻辑;
                // ③在@Validate属性中加入customWay属性，将customWay属性值设置为自定义属性A的class对象（也就是ValidateType.custom和customWay必须同时使用！！）
                /*
                *   @Validate(type = ValidateType.custom, customWay = customFirst.class)
                    public static class customFirst implements ValidatorHandler {
                        @Override
                        public boolean validate(Object fieldInstance, Validate validate) {
                            return Boolean.FALSE;
                        }
                    }
                * */

                //得到该自定义内部类的Class对象
                Class t = validate.customWay();
                /*判定用于使用自定义后，是否传入了自定义的逻辑。得到该类实现的接口的类型数组。
                  为了判断其是继承了ValidatorHandler接口的。其实这步可以通过泛型来规范*/
                Type[] types = t.getGenericInterfaces();
                if (!ArrayUtils.isEmpty(types) && ArrayUtils.contains(types, ValidatorHandler.class)) {

                    //判定customWay是自定义的，得到用户自定义判定结果
                    flag = (validate.customWay().newInstance()).validate(fieldInstance, validate);

                    //组装错误通知消息(注解中有用户自定义message就使用，否则使用服务默认的)并马上返回值
                    logger.debug("字段" + "   " + param.getParamName() + "   " + ValidatorUtil.defaultIfEmpty(validate.message(), validateType.getMessage()));
                    message = ValidatorUtil.defaultIfEmpty(validate.message(), validateType.getMessage());
                    return new ValidatorResult().setValidateResult(flag).setMessage(message);
                } else {
                    flag = false;
                    message = validateType.getMessage();
                    return new ValidatorResult().setValidateResult(flag).setMessage(message);
                }
            }
        }
        return new ValidatorResult().setValidateResult(flag).setMessage(message);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
