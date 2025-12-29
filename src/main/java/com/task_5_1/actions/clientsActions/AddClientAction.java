package com.task_5_1.actions.clientsActions;

import com.task_5_1.actions.IAction;
import com.task_6_2.ClientException;
import com.task_8_2.interfaces.IBookStore;

import java.util.Scanner;

public class AddClientAction implements IAction {
    Scanner scanner = new Scanner(System.in);
    IBookStore bookStore;

    public AddClientAction(IBookStore bookStore) {
        this.bookStore = bookStore;
    }

    @Override
    public void execute() throws ClientException {
        try {
            System.out.println("Введите ФИО клиента: ");
            String name = scanner.nextLine();
            System.out.println("Введите возраст клиента: ");
            int age = scanner.nextInt();
            scanner.nextLine();
            bookStore.addClient(name, age);
        }
        catch (Exception exception) {
            scanner.nextLine();
            throw new ClientException("Введены невалидные данные клиента. Попробуйте снова.");
        }
    }
}
