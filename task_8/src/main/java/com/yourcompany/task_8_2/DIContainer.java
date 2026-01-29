package com.yourcompany.task_8_2;

import com.yourcompany.task_8_2.annotations.Inject;
import com.yourcompany.task_8_2.annotations.PostConstruct;
import com.yourcompany.task_8_2.interfaces.IBeanConfigurator;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DIContainer {
    private static DIContainer instance = new DIContainer();
    private DIContainer(){
        configurator = new JavaBeanConfigurator();
    }
    private IBeanConfigurator configurator;

    private final Map<Class<?>, Object> singletonCache = new ConcurrentHashMap<>();

    public static DIContainer getInstance() {
        return instance;
    }

    public <T> T getBean(Class<T> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<? extends T> implementationClass = clazz;
        if (implementationClass.isInterface()) {
            implementationClass = configurator.getInterfaceImplementation(clazz);
        }

        if (singletonCache.containsKey(implementationClass)) {
            return (T) singletonCache.get(implementationClass);
        }

        T bean = implementationClass.getDeclaredConstructor().newInstance();

        singletonCache.put(implementationClass, bean);

        for (Field field : Arrays.stream(implementationClass.getDeclaredFields()).filter(field -> field.isAnnotationPresent(Inject.class)).toList()) {
            field.setAccessible(true);
            field.set(bean, instance.getBean(field.getType()));
        }

        for (Method method : bean.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
                method.setAccessible(true);
                method.invoke(bean);
            }
        }

        return bean;
    }
}
