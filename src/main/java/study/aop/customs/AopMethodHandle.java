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
            List<MethodWrapper> methodIntercepters = getMethodIntercepters(aopBean);
            methods.addAll(methodIntercepters);
        }
        return methods;
    }

    private List<MethodWrapper> getMethodIntercepters(Object aopBean) {
        List<MethodWrapper> allMethods = new ArrayList<MethodWrapper>();
        Method[] methods = aopBean.getClass().getMethods();
        for (Method method : methods) {
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
