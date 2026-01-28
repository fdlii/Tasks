package com.task_5_1;

import com.task_14.ApplicationConfig;
import com.task_8_2.interfaces.IMenuController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.lang.reflect.InvocationTargetException;

public class Program {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);

        IMenuController menuController = context.getBean(MenuController.class);
        menuController.run();
    }
}
