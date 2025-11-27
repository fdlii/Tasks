package task_5_1;

import task_3_4.BookStore;

public class Program {
    public static void main(String[] args) {
        new BookStore(); // для единственной инициализации конфигурации
        MenuController menuController = MenuController.getInstance();
        menuController.run();
    }
}
