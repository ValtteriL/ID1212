package se.kth.id1212.appserv.bank.domain;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
class BeanFactory implements ApplicationContextAware {
    private static ApplicationContext context;

    /**
     * Retrieves a bean of the specified class from the application context.
     * This method is intended for use only by JPA entities, other classes
     * should use @Autowired instead.
     *
     * @param beanClass The type of the bean to retrieve.
     */
    static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }
}