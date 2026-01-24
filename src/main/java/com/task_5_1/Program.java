package com.task_5_1;

import com.task_14.AppConfig;
import com.task_8_2.DIContainer;
import com.task_8_2.interfaces.IMenuController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.lang.reflect.InvocationTargetException;

public class Program {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        IMenuController menuController = context.getBean(MenuController.class);
        menuController.run();
    }
}
