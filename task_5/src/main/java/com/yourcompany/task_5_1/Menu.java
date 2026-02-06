package com.yourcompany.task_5_1;

import com.yourcompany.task_5_1.actions.IAction;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    private String name;
    public List<MenuItem> menuItems;

    public Menu(String name) {
        this.name = name;
        menuItems = new ArrayList<>();
    }

    public void addItem(String title, IAction action, Menu nextMenu) {
        menuItems.add(new MenuItem(title, action, nextMenu));
    }

    public String getName() {
        return name;
    }
}
