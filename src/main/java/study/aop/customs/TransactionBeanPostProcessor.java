package study.aop.customs;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

//@Component
public class TransactionBeanPostProcessor implements BeanPostProcessor {

    public Object postProcessAfterInitialization(final Object bean, String beanName) throws BeansException {
        boolean annotationPresent = bean.getClass().isAnnotationPresent(CustomTransaction.class);
        if (!annotationPresent) {
            return bean;
        }
        return new TransactionProxy(bean).getProxy();
    }
}
