package com.task_5_1.actions.ordersActions;

import com.task_5_1.actions.IAction;
import com.task_3_4.BookStore;
import com.task_6_2.OrderExeption;
import com.task_8_2.annotations.Inject;
import com.task_8_2.interfaces.IBookStore;

import java.util.Scanner;

public class CancelOrderAction implements IAction {
    Scanner scanner = new Scanner(System.in);
    IBookStore bookStore;

    public CancelOrderAction(IBookStore bookStore) {
        this.bookStore = bookStore;
    }

    @Override
    public void execute() throws OrderExeption {
        try {
            System.out.println("Введите идентификатор заказа: ");
            int id = scanner.nextInt();
            scanner.nextLine();
            bookStore.cancelOrder(id);
        }
        catch (Exception exception) {
            throw new OrderExeption("Введены невалидные данные заказа. Попробуйте снова.");
        }
    }
}
