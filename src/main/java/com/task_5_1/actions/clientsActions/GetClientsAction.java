package com.task_5_1.actions.clientsActions;

import com.task_5_1.actions.IAction;
import com.task_3_4.BookStore;
import com.task_3_4.Client;
import com.task_8_2.annotations.Inject;
import com.task_8_2.interfaces.IBookStore;
import org.hibernate.HibernateException;

import java.sql.SQLException;

public class GetClientsAction implements IAction {
    IBookStore bookStore;

    public GetClientsAction(IBookStore bookStore) {
        this.bookStore = bookStore;
    }

    @Override
    public void execute() {
        try {
            System.out.println("Список клиентов: ");
            for (Client client : bookStore.getClients()) {
                System.out.printf("ФИО : %s, возраст: %d.%n", client.getName(), client.getAge());
            }
        } catch (HibernateException ex) {
            System.out.println("Ошибка при получении клиентов из БД.");
        }
    }
}
