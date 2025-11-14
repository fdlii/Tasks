package actions.requestsActions;

import actions.IAction;
import task_3_4.BookStore;
import task_6_2.BookExeption;

import java.util.Scanner;

public class MakeRequestAction implements IAction {
    Scanner scanner = new Scanner(System.in);

    @Override
    public void execute() throws BookExeption {
        try {
            System.out.println("Введите название книги: ");
            String name = scanner.nextLine();
            System.out.println("Введите число книг на запрос: ");
            int count = scanner.nextInt();
            BookStore.makeRequest(name, count);
        }
        catch (Exception exception) {
            throw new BookExeption("Введено невалидное значение книг на запрос. Попробуйте снова.");
        }
    }
}
