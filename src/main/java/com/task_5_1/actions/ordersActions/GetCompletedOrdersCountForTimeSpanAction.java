package com.task_5_1.actions.ordersActions;

import com.task_5_1.actions.IAction;
import com.task_6_2.OrderException;
import com.task_8_2.interfaces.IBookStore;

import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;

public class GetCompletedOrdersCountForTimeSpanAction implements IAction {
    Scanner scanner = new Scanner(System.in);
    IBookStore bookStore;

    public GetCompletedOrdersCountForTimeSpanAction(IBookStore bookStore) {
        this.bookStore = bookStore;
    }

    @Override
    public void execute() throws OrderException {
        try {
            System.out.println("Введите дату \"c\": ");
            Date dateFrom = new Date(scanner.nextLine());
            System.out.println("Введите дату \"по\": ");
            Date dateTo = new Date(scanner.nextLine());
            System.out.println("Число заказов: " + bookStore.getCompletedOrdersCountForTimeSpan(dateFrom, dateTo));
        }
        catch (SQLException exception) {
            System.out.println("Не удалось получить число заказов.");
        }
        catch (Exception exception) {
            scanner.nextLine();
            throw new OrderException("Введены невалидные даты. Формат ввода даты - дд/мм/гггг. Попробуйте снова.");
        }
    }
}
