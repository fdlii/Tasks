package com.task_5_1;

import com.task_5_1.actions.IAction;
import com.task_6_2.BookExeption;
import com.task_6_2.ClientExeption;
import com.task_6_2.OrderExeption;

import java.io.IOException;

public class MenuItem {
    private String title;
    private IAction action;
    private Menu nextMenu;

    public MenuItem(String title, IAction action, Menu nextMenu) {
        this.title = title;
        this.action = action;
        this.nextMenu = nextMenu;
    }

    public void doAction() throws IOException, BookExeption, ClientExeption, OrderExeption {
        action.execute();
    }

    public Menu getNextMenu() {
        return nextMenu;
    }

    public String getTitle() {
        return title;
    }
}
