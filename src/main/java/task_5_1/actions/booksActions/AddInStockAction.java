package task_5_1.actions.booksActions;

import task_5_1.actions.IAction;
import task_3_4.BookStore;
import task_6_2.BookExeption;

import java.util.Scanner;

public class AddInStockAction implements IAction {
    Scanner scanner = new Scanner(System.in);

    @Override
    public void execute() throws BookExeption {
        try {
            System.out.println("Введите название книги: ");
            String name = scanner.nextLine();
            System.out.println("Введите число книг для добавления: ");
            int count = scanner.nextInt();
            BookStore.addInStock(name, count);
        }
        catch (Exception exception) {
            throw new BookExeption("Введены невалидные данные книги. Попробуйте снова.");
        }
    }
}
