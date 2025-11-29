package com.task_8_2;

import com.task_8_2.annotations.Inject;
import com.task_8_2.annotations.PostConstruct;
import com.task_8_2.interfaces.IBeanConfigurator;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class DIContainer {
    private static DIContainer instance = new DIContainer();
    private DIContainer(){
        configurator = new JavaBeanConfigurator();
    }
    private IBeanConfigurator configurator;

    public static DIContainer getInstance() {
        return instance;
    }

    public <T> T getBean(Class<T> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<? extends T> implementationClass = clazz;
        if (implementationClass.isInterface()) {
            implementationClass = configurator.getInterfaceImplementation(clazz);
        }

        T bean = implementationClass.getDeclaredConstructor().newInstance();

        for (Field field : Arrays.stream(implementationClass.getDeclaredFields()).filter(field -> field.isAnnotationPresent(Inject.class)).toList()) {
            field.setAccessible(true);
            field.set(bean, instance.getBean(field.getType()));
        }

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
                method.setAccessible(true);
                method.invoke(bean);
            }
        }

        return bean;
    }
}
