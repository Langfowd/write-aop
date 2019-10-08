package study.aop.customs;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class AopProxy implements InvocationHandler {
    List<MethodWrapper> targetMethods;
    Object targetBean;

    public AopProxy(List<MethodWrapper> finalMethods, Object bean) {
        this.targetMethods = finalMethods;
        this.targetBean = bean;
    }

    public Object getProxy() {
        return Proxy.newProxyInstance(targetBean.getClass().getClassLoader(),targetBean.getClass().getInterfaces(),this);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 获取实现类的方法
        Method m = targetBean.getClass().getMethod(method.getName(), method.getParameterTypes());

        List<MethodWrapper> applyMethodWapper = new ArrayList<MethodWrapper>();
        // 筛选适用于该调用方法的增强方法
        for (MethodWrapper targetMethod : targetMethods) {
            if (m.isAnnotationPresent(targetMethod.getAnnotatitionValue())) {
                applyMethodWapper.add(targetMethod);
            }
        }

        // 分别存储3个不同时期运行的注解的方法
        List<MethodWrapper> beforeMethods = new ArrayList<MethodWrapper>();
        List<MethodWrapper> afterMethods = new ArrayList<MethodWrapper>();
        List<MethodWrapper> aroundMethods = new ArrayList<MethodWrapper>();

        for (MethodWrapper targetMethod : applyMethodWapper) {
            if (targetMethod.getInteceptMethodAnnationName().equals(CustomBefore.class.getName())) {
                beforeMethods.add(targetMethod);
            }
            if (targetMethod.getInteceptMethodAnnationName().equals(CustomAfter.class.getName())) {
                afterMethods.add(targetMethod);
            }
            if (targetMethod.getInteceptMethodAnnationName().equals(CustomAround.class.getName())) {
                aroundMethods.add(targetMethod);
            }
        }
        // 封装相关信息
        JoinPoint joinPoint = new JoinPoint(targetBean, args, m);

        // 前置处理
        for (MethodWrapper beforeMethod : beforeMethods) {
            // 有参数就运行带参数的方法
            if (beforeMethod.getParameterTypes().length > 0) {
                beforeMethod.getMethod().invoke(beforeMethod.getTarget(), joinPoint);
            } else {
                beforeMethod.getMethod().invoke(beforeMethod.getTarget());
            }
        }
        // 具体真是的类运行该方法
        Object result = method.invoke(targetBean, args);
        // 环绕处理
        for (MethodWrapper aroundMethod : aroundMethods) {
            // 环绕必须要求有返回值
            if (aroundMethod.getMethod().getReturnType().equals(Void.class)) {
                throw new RuntimeException("环绕通知需要返回对应的方法值");
            }
            // 将around注解方法的结果覆盖本身结果
            result = aroundMethod.getMethod().invoke(aroundMethod.getTarget(), joinPoint);
        }


        // 后置处理
        for (MethodWrapper afterMethod : afterMethods) {
            if (afterMethod.getParameterTypes().length > 0) {
                afterMethod.getMethod().invoke(afterMethod.getTarget(), joinPoint);
            } else {
                afterMethod.getMethod().invoke(afterMethod.getTarget());
            }
        }

        return result;
    }

}
