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

        Method m = targetBean.getClass().getMethod(method.getName(), method.getParameterTypes());

        List<MethodWrapper> applyMethodWapper = new ArrayList<MethodWrapper>();
        for (MethodWrapper targetMethod : targetMethods) {
            if (m.isAnnotationPresent(targetMethod.getAnnotatitionValue())) {
                applyMethodWapper.add(targetMethod);
            }
        }


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
        JoinPoint joinPoint = new JoinPoint(targetBean, args, m);
        for (MethodWrapper beforeMethod : beforeMethods) {
            if (beforeMethod.getParameterTypes().length > 0) {
                beforeMethod.getMethod().invoke(beforeMethod.getTarget(), joinPoint);
            } else {
                beforeMethod.getMethod().invoke(beforeMethod.getTarget());
            }
        }
        Object result = method.invoke(targetBean, args);
        for (MethodWrapper aroundMethod : aroundMethods) {
            if (aroundMethod.getMethod().getReturnType().equals(Void.class)) {
                throw new RuntimeException("环绕通知需要返回对应的方法值");
            }
            result = aroundMethod.getMethod().invoke(aroundMethod.getTarget(), joinPoint);
        }



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
