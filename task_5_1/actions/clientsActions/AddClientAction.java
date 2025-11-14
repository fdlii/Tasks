package actions.clientsActions;

import actions.IAction;
import task_3_4.BookStore;
import task_6_2.BookExeption;
import task_6_2.ClientExeption;

import java.util.Scanner;

public class AddClientAction implements IAction {
    Scanner scanner = new Scanner(System.in);

    @Override
    public void execute() throws ClientExeption {
        try {
            System.out.println("Введите ФИО клиента: ");
            String name = scanner.nextLine();
            System.out.println("Введите возраст клиента: ");
            int age = scanner.nextInt();
            BookStore.addClient(name, age);
        }
        catch (Exception exception) {
            throw new ClientExeption("Введены невалидные данные клиента. Попробуйте снова.");
        }
    }
}
