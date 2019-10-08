package study.aop.customs;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AopMethodHandle {
    List<Object> aopBeans;



    public AopMethodHandle(List<Object> contextBeans) {
        aopBeans = contextBeans;
    }

    public List<MethodWrapper> getMethodIntercepters() {
        List<MethodWrapper> methods = new ArrayList<MethodWrapper>();
        for (Object aopBean : aopBeans) {
            // 遍历带有@CustomAspect的类，获取带有3个注解的方法
            List<MethodWrapper> methodIntercepters = getMethodIntercepters(aopBean);
            methods.addAll(methodIntercepters);
        }
        return methods;
    }

    private List<MethodWrapper> getMethodIntercepters(Object aopBean) {
        List<MethodWrapper> allMethods = new ArrayList<MethodWrapper>();
        // 获取所有的共有方法
        Method[] methods = aopBean.getClass().getMethods();
        for (Method method : methods) {
            // 遍历获取带有以下3个注解的方法并封装成MethodWrapper
            if (method.isAnnotationPresent(CustomBefore.class)) {
                CustomBefore customBefore = method.getAnnotation(CustomBefore.class);
                allMethods.add(new MethodWrapper(customBefore.value(),method,aopBean,CustomBefore.class.getName()));
                continue;
            }
            if (method.isAnnotationPresent(CustomAround.class)) {
                CustomAround customAround = method.getAnnotation(CustomAround.class);
                allMethods.add(new MethodWrapper(customAround.value(),method,aopBean, CustomAround.class.getName()));
                continue;
            }
            if (method.isAnnotationPresent(CustomAfter.class)) {
                CustomAfter customAfter = method.getAnnotation(CustomAfter.class);
                allMethods.add(new MethodWrapper(customAfter.value(),method,aopBean, CustomAfter.class.getName()));
            }
        }
        return allMethods;
    }

    public  List<MethodWrapper> applyBeanIntercepters(Object bean, List<MethodWrapper> methodIntercepters) {
        List<MethodWrapper> methodWrappers = new ArrayList<MethodWrapper>();
        for (MethodWrapper methodIntercepter : methodIntercepters) {
            if (methodIntercepter.canApply(bean)) {
                methodWrappers.add(methodIntercepter);
            }
        }
        return methodWrappers;

    }
}
