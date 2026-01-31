package com.yourcompany.task_5_1.actions.ordersActions;

import com.yourcompany.exceptions.OrderException;
import com.yourcompany.task_5_1.actions.IAction;
import com.yourcompany.task_3_4.interfaces.IBookStore;
import org.hibernate.HibernateException;

import java.util.Scanner;

public class CompleteOrderAction implements IAction {
    Scanner scanner = new Scanner(System.in);
    IBookStore bookStore;

    public CompleteOrderAction(IBookStore bookStore) {
        this.bookStore = bookStore;
    }

    @Override
    public void execute() throws OrderException {
        try {
            System.out.println("Введите идентификатор заказа: ");
            long id = scanner.nextLong();
            scanner.nextLine();
            bookStore.completeOrder(id);
        }
        catch (HibernateException exception) {
            System.out.println("Не удалось выполнить заказ в БД.");
        }
        catch (OrderException exception) {
            System.out.println(exception.getMessage());
        }
        catch (Exception exception) {
            scanner.nextLine();
            throw new OrderException("Введены невалидные данные заказа. Попробуйте снова.");
        }
    }
}
