package study.aop;

import org.springframework.stereotype.Component;
import study.aop.customs.CustomAfter;
import study.aop.customs.CustomAround;
import study.aop.customs.CustomAspect;
import study.aop.customs.JoinPoint;

import java.lang.reflect.Method;

@CustomAspect
@Component
public class CustomAop {



    @CustomAround(value = NumIncr.class)
    public int handleIncr(JoinPoint joinPoint) throws Throwable {
        Method method = joinPoint.getMethod();
        NumIncr annotation = method.getAnnotation(NumIncr.class);
        if (method.getReturnType() != int.class) {
            throw new RuntimeException("method return type must be int");
        }
        int value = annotation.value();
        int invoke = (Integer) method.invoke(joinPoint.getTargetObject(), joinPoint.getArgs());
        return invoke+value;
    }


    @CustomAfter(value = NumIncr.class)
    public void after(JoinPoint joinPoint) throws Throwable {
        Method method = joinPoint.getMethod();
        Object result = method.invoke(joinPoint.getTargetObject(), joinPoint.getArgs());
        System.out.println("该方法的原始值为"+result);
    }



}
