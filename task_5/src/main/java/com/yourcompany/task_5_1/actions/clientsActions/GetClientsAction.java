package com.yourcompany.task_5_1.actions.clientsActions;

import com.yourcompany.models.Client;
import com.yourcompany.task_5_1.actions.IAction;
import com.yourcompany.task_3_4.interfaces.IBookStore;
import org.hibernate.HibernateException;

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
