package study.aop.customs;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TransactionProxy implements InvocationHandler {
    Object target;
    public TransactionProxy(Object bean) {
        target = bean;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("我进来了");
        return method.invoke(target,args);
    }

    public Object getProxy() {
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),target.getClass().getInterfaces(),this);
    }
}
