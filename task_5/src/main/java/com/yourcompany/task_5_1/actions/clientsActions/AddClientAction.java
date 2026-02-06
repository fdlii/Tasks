package com.yourcompany.task_5_1.actions.clientsActions;

import com.yourcompany.exceptions.ClientException;
import com.yourcompany.task_5_1.actions.IAction;
import com.yourcompany.task_3_4.interfaces.IBookStore;
import org.hibernate.HibernateException;

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
        catch (HibernateException exception) {
            System.out.println("Не удалось добавить клиента в БД.");
        }
        catch (Exception exception) {
            scanner.nextLine();
            throw new ClientException("Введены невалидные данные клиента. Попробуйте снова.");
        }
    }
}
