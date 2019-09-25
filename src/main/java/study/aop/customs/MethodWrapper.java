package study.aop.customs;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class MethodWrapper {
    private Class<? extends Annotation> annotatitionValue;
    private String inteceptMethodAnnationName;
    private Method method;
    private Class targetClass;
    private Class<?>[] parameterTypes;
    private Object target;

    public MethodWrapper(Class<? extends Annotation> value, Method method, Object target, String inteceptMethodAnnationName) {
        annotatitionValue = value;
        this.inteceptMethodAnnationName = inteceptMethodAnnationName;
        this.method = method;
        this.targetClass = method.getDeclaringClass();
        this.parameterTypes = method.getParameterTypes();
        this.target = target;
    }

    boolean canApply(Object bean) {
        Method[] methods = bean.getClass().getMethods();
        for (Method m : methods) {
            return m.isAnnotationPresent(annotatitionValue);
        }
        return false;
    }

    /**
     * Gets the value of target.
     *
     * @return the value of target
     */
    public Object getTarget() {
        return target;
    }

    /**
     * Gets the value of annotatitionValue.
     *
     * @return the value of annotatitionValue
     */
    public Class<? extends Annotation> getAnnotatitionValue() {
        return annotatitionValue;
    }

    /**
     * Gets the value of method.
     *
     * @return the value of method
     */
    public Method getMethod() {
        return method;
    }

    /**
     * Gets the value of targetClass.
     *
     * @return the value of targetClass
     */
    public Class getTargetClass() {
        return targetClass;
    }

    /**
     * Gets the value of parameterTypes.
     *
     * @return the value of parameterTypes
     */
    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    /**
     * Gets the value of inteceptMethodAnnationName.
     *
     * @return the value of inteceptMethodAnnationName
     */
    public String getInteceptMethodAnnationName() {
        return inteceptMethodAnnationName;
    }
}
