package actions.requestsActions;

import actions.IAction;
import task_3_4.BookStore;

import java.util.Date;
import java.util.Scanner;

public class MakeRequestAction implements IAction {
    Scanner scanner = new Scanner(System.in);

    @Override
    public void execute() {
        System.out.println("Введите название книги: ");
        String name = scanner.nextLine();
        System.out.println("Введите число книг на запрос: ");
        int count = scanner.nextInt();
        BookStore.makeRequest(name, count);
    }
}
