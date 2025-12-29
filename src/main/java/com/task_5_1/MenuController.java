package com.task_5_1;

import com.task_6_2.BookException;
import com.task_6_2.ClientException;
import com.task_6_2.OrderException;
import com.task_8_2.annotations.Inject;
import com.task_8_2.annotations.PostConstruct;
import com.task_8_2.interfaces.IBookStore;
import com.task_8_2.interfaces.IFileManager;
import com.task_8_2.interfaces.IMenuController;

import java.io.IOException;
import java.util.Scanner;

public class MenuController implements IMenuController {
    private Scanner scanner = new Scanner(System.in);
    private static Builder builder;
    private static Menu currentMenu;
    @Inject
    private IBookStore bookStore;
    @Inject
    private IFileManager fileManager;

    public MenuController() {
        builder = new Builder();
    }

    @PostConstruct
    public void initialize() {
        builder.buildMenu(bookStore);
    }

    public void run() {
        currentMenu = builder.getRootMenu();
        int count = printMenu();
        int choice = scanner.nextInt();
        navigate(choice, count);
    }

    private void runNext() {
        int count = printMenu();
        int choice = scanner.nextInt();
        navigate(choice, count);
    }

    public void navigate(int index, int count) {
        if (index > currentMenu.menuItems.size() && index != count) {
            System.out.println("Невалидное значение выбора. Попробуйте снова.");
            run();
        }
        else if (index == count) {
            System.exit(0);
//            try {
//                fileManager.serializeObjects(bookStore.getRequests(), "requests");
//                fileManager.serializeObjects(bookStore.getBooks(), "books");
//                fileManager.serializeObjects(bookStore.getOrders(), "orders");
//                fileManager.serializeObjects(bookStore.getClients(), "clients");
//                System.exit(0);
//
//            }
//            catch (IOException ex) {
//                System.out.println("Не удалось сериализовать объекты в JSON.");
//                System.exit(1);
//            }
        }
        else {
            if (currentMenu.menuItems.get(index - 1).getNextMenu() != null) {
                currentMenu = currentMenu.menuItems.get(index - 1).getNextMenu();
                runNext();
            }
            else {
                try
                {
                    currentMenu.menuItems.get(index - 1).doAction();
                }
                catch (IOException ex) {
                    System.out.println("Ошибка при чтении файла. Попробуйте снова.");
                }
                catch (BookException | ClientException | OrderException ex) {
                    System.out.println(ex.getMessage());
                }
                finally {
                    run();
                }
            }
        }
    }

    public int printMenu() {
        int count = 1;
        System.out.println(currentMenu.getName());
        for (MenuItem item : currentMenu.menuItems) {
            System.out.println(String.format("%d. %s", count, item.getTitle()));
            count++;
        }
        System.out.println(String.format("%d. %s", count, "Выход"));
        return count;
    }
}
