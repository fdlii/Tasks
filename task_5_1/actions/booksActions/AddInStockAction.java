package actions.booksActions;

import actions.IAction;
import task_3_4.BookStore;

import java.util.Scanner;

public class AddInStockAction implements IAction {
    Scanner scanner = new Scanner(System.in);

    @Override
    public void execute() {
        System.out.println("Введите название книги: ");
        String name = scanner.nextLine();
        System.out.println("Введите число книг для добавления: ");
        int count = scanner.nextInt();
        BookStore.addInStock(name, count);
    }
}
