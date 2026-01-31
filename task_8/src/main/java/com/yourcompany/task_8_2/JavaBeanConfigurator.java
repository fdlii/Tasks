package com.yourcompany.task_8_2;

import org.reflections.Reflections;
import com.yourcompany.task_8_2.interfaces.IBeanConfigurator;

public class JavaBeanConfigurator implements IBeanConfigurator {

    final Reflections scanner;

    public JavaBeanConfigurator() {
        scanner = new Reflections("com");
    }
    @Override
    public <T> Class<? extends T> getInterfaceImplementation(Class<T> implementation) {
        Class<? extends T> clas = scanner.getSubTypesOf(implementation).stream().findFirst().get();
        return clas;
    }
}
