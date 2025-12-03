package actions.booksActions;

import actions.IAction;
import task_3_4.BookStore;
import task_6_2.BookExeption;

import java.util.Scanner;

public class DebitFromStockAction implements IAction {
    Scanner scanner = new Scanner(System.in);

    @Override
    public void execute() throws BookExeption {
        try {
            System.out.println("Введите название книги: ");
            String name = scanner.nextLine();
            BookStore.debitFromStock(name);
        }
        catch (Exception exception) {
            throw new BookExeption("Введены невалидные данные книги. Попробуйте снова.");
        }
    }
}
