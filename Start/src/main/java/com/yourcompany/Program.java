package com.yourcompany;

import com.yourcompany.task_14.ApplicationConfig;
import com.yourcompany.task_5_1.MenuController;
import com.yourcompany.task_5_1.interfaces.IMenuController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.lang.reflect.InvocationTargetException;

public class Program {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);

        IMenuController menuController = context.getBean(MenuController.class);
        menuController.run();
    }
}
