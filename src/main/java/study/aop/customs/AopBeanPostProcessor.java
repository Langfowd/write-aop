package study.aop.customs;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Component
public class AopBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext context;



    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 判断是否有增强方法
        boolean hasAspect  =  hasAspectMethod(bean);
        if (!hasAspect) {
            return bean;
        }
        // 获取所有@CustomAspect注解的类
        List<Object> contextBeans = getBeansForAnnotation(CustomAspect.class);
        // 获取所有的增强器
        AopMethodHandle handle = new AopMethodHandle(contextBeans);
        // 获取带有注解增强的逻辑方法
        List<MethodWrapper> methodIntercepters = handle.getMethodIntercepters();
        // 筛选出最终适合该方法的增强器
        List<MethodWrapper> finalMethods = handle.applyBeanIntercepters(bean,methodIntercepters);
        // 创建代理对象
        return new AopProxy(finalMethods,bean).getProxy();
    }

    private List<Object> getBeansForAnnotation(Class<? extends Annotation> clazz) {
        String[] beanNames = context.getBeanNamesForAnnotation(clazz);
        List<Object> contextBeans = new ArrayList<Object>();
        for (String name : beanNames) {
            Object contextBean = context.getBean(name);
            contextBeans.add(contextBean);
        }
        return contextBeans;
    }

    private boolean hasAspectMethod(Object bean) {
        // 获取这个被代理类的所有方法
        Method[] methods = bean.getClass().getMethods();
        List<Class> list = new ArrayList<Class>();
        // 从ioc容器中获取所有带有@CustomAspect的类
        List<Object> contextBeans = getBeansForAnnotation(CustomAspect.class);
        for (Object contextBean : contextBeans) {
            // 获取带有@CustomAspect的类的所有共有方法
            Method[] ms = contextBean.getClass().getMethods();
            // 获取带有@CustomAfter @CustomBefore @CustomAround方法上注解，并且获取注解value的值
            for (Method m : ms) {
                CustomAfter customAfter = m.getAnnotation(CustomAfter.class);
                if (customAfter != null) {
                    list.add(customAfter.value());
                }
                CustomBefore customBefore = m.getAnnotation(CustomBefore.class);
                if (customBefore != null) {
                    list.add(customBefore.value());
                }
                CustomAround customAround = m.getAnnotation(CustomAround.class);
                if (customAround != null) {
                    list.add(customAround.value());
                }
            }
        }

        for (Class aClass : list) {
            for (Method method : methods) {
                // 遍历被代理类的方法，判断是否存在该注解
                boolean annotationPresent = method.isAnnotationPresent(aClass);
                if (annotationPresent) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
