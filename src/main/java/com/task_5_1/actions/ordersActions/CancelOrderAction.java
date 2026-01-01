package com.task_5_1.actions.ordersActions;

import com.task_5_1.actions.IAction;
import com.task_6_2.OrderException;
import com.task_8_2.interfaces.IBookStore;

import java.sql.SQLException;
import java.util.Scanner;

public class CancelOrderAction implements IAction {
    Scanner scanner = new Scanner(System.in);
    IBookStore bookStore;

    public CancelOrderAction(IBookStore bookStore) {
        this.bookStore = bookStore;
    }

    @Override
    public void execute() throws OrderException {
        try {
            System.out.println("Введите идентификатор заказа: ");
            int id = scanner.nextInt();
            scanner.nextLine();
            bookStore.cancelOrder(id);
        }
        catch (SQLException exception) {
            System.out.println("Не удалось обновить заказ в БД.");
        }
        catch (Exception exception) {
            scanner.nextLine();
            throw new OrderException("Введены невалидные данные заказа. Попробуйте снова.");
        }
    }
}
