package com.yourcompany.task_5_1;

import com.yourcompany.exceptions.BookException;
import com.yourcompany.exceptions.ClientException;
import com.yourcompany.exceptions.OrderException;
import com.yourcompany.task_5_1.actions.IAction;

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

    public void doAction() throws IOException, BookException, ClientException, OrderException {
        action.execute();
    }

    public Menu getNextMenu() {
        return nextMenu;
    }

    public String getTitle() {
        return title;
    }
}
