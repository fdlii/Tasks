package actions.clientsActions;

import actions.IAction;
import task_3_4.Book;
import task_3_4.BookStore;

import java.util.Scanner;

public class AddClientAction implements IAction {
    Scanner scanner = new Scanner(System.in);

    @Override
    public void execute() {
        System.out.println("Введите ФИО клиента: ");
        String name = scanner.nextLine();
        System.out.println("Введите возраст клиента: ");
        int age = scanner.nextInt();
        BookStore.addClient(name, age);
    }
}
