package study.aop.customs;

import java.lang.reflect.Method;

public class JoinPoint {
    private Object targetObject;
    private Object[] args;
    private Method method;

    public JoinPoint(Object targetObject, Object[] args, Method method) {
        this.targetObject = targetObject;
        this.args = args;
        this.method = method;
    }

    /**
     * Gets the value of targetObject.
     *
     * @return the value of targetObject
     */
    public Object getTargetObject() {
        return targetObject;
    }

    /**
     * Gets the value of args.
     *
     * @return the value of args
     */
    public Object[] getArgs() {
        return args;
    }

    /**
     * Gets the value of method.
     *
     * @return the value of method
     */
    public Method getMethod() {
        return method;
    }
}
