package actions.booksActions;

import actions.IAction;
import task_3_4.BookStore;

import java.util.Scanner;

public class DeleteBookAction implements IAction {
    Scanner scanner = new Scanner(System.in);

    @Override
    public void execute() {
        System.out.println("Введите название книги: ");
        String name = scanner.nextLine();
        BookStore.deleteBook(name);
    }
}
