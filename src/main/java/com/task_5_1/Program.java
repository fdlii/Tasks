package com.task_5_1;

import com.task_8_2.DIContainer;
import com.task_8_2.annotations.Inject;
import com.task_8_2.interfaces.IMenuController;

import java.lang.reflect.InvocationTargetException;

public class Program {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        DIContainer diContainer = null;
            diContainer = run();
            MenuController menuController = diContainer.getBean(MenuController.class);
            menuController.run();
    }

    public static DIContainer run() {
        return DIContainer.getInstance();
    }
}
