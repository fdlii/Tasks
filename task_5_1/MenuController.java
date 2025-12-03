import task_3_4.Book;
import task_6_2.BookExeption;
import task_6_2.ClientExeption;
import task_6_2.OrderExeption;

import java.io.IOException;
import java.util.Scanner;

public class MenuController {
    private Scanner scanner = new Scanner(System.in);
    private static MenuController menuController;
    private static Builder builder;
    private static Menu currentMenu;

    private MenuController(){
        builder = new Builder();
    }

    public static MenuController getInstance() {
        if (menuController == null) {
            menuController = new MenuController();
            builder = new Builder();
            builder.buildMenu();
        }
        return menuController;
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
                catch (BookExeption | ClientExeption | OrderExeption ex) {
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
