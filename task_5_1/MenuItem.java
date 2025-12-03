import actions.IAction;
import task_6_2.BookExeption;
import task_6_2.ClientExeption;
import task_6_2.OrderExeption;

import java.io.IOException;
import java.util.List;

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
