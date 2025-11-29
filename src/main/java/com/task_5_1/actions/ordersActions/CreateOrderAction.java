package com.task_5_1.actions.ordersActions;

import com.task_5_1.actions.IAction;
import com.task_3_4.BookStore;
import com.task_6_2.OrderExeption;
import com.task_8_2.annotations.Inject;
import com.task_8_2.interfaces.IBookStore;

import java.util.Date;
import java.util.Scanner;

public class CreateOrderAction implements IAction {
    Scanner scanner = new Scanner(System.in);
    IBookStore bookStore;

    public CreateOrderAction(IBookStore bookStore) {
        this.bookStore = bookStore;
    }

    @Override
    public void execute() throws OrderExeption {
        try {
            System.out.println("Введите скидку в заказе клиента: ");
            double discount = scanner.nextDouble();
            scanner.nextLine();
            System.out.println("Введите дату исполнения: ");
            Date date = new Date(scanner.nextLine());
            System.out.println("Введите ФИО клиента: ");
            String name = scanner.nextLine();
            System.out.println("Введите количество книг в заказе клиента: ");
            int count = scanner.nextInt();
            scanner.nextLine();
            int oldCount = count;
            String bookNames[] = new String[count];
            while (count > 0) {
                System.out.println(String.format("Введите название %d книги: ", oldCount - count + 1));
                bookNames[oldCount - count] = scanner.nextLine();
                count--;
            }
            bookStore.createOrder(discount, date, name, bookNames);
        }
        catch (Exception exception) {
            throw new OrderExeption("Введены невалидные данные заказа. Попробуйте снова.");
        }
    }
}
