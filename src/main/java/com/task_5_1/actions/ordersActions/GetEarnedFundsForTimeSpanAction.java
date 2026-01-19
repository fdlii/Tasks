package com.task_5_1.actions.ordersActions;

import com.task_5_1.actions.IAction;
import com.task_6_2.OrderException;
import com.task_8_2.interfaces.IBookStore;

import java.util.Date;
import java.util.Scanner;

public class GetEarnedFundsForTimeSpanAction implements IAction {
    Scanner scanner = new Scanner(System.in);
    IBookStore bookStore;

    public GetEarnedFundsForTimeSpanAction(IBookStore bookStore) {
        this.bookStore = bookStore;
    }

    @Override
    public void execute() throws OrderException {
        try {
            System.out.println("Введите дату \"c\": ");
            Date dateFrom = new Date(scanner.nextLine());
            System.out.println("Введите дату \"по\": ");
            Date dateTo = new Date(scanner.nextLine());
            System.out.println(bookStore.getEarnedFundsForTimeSpan(dateFrom, dateTo));
        }
        catch (Exception exception) {
            scanner.nextLine();
            throw new OrderException("Введены невалидные даты. Формат ввода даты - дд/мм/гггг. Попробуйте снова.");
        }
    }
}
