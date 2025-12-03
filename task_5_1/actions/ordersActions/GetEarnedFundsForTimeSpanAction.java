package actions.ordersActions;

import actions.IAction;
import task_3_4.BookStore;
import task_3_4.Order;
import task_6_2.OrderExeption;

import java.util.Date;
import java.util.Scanner;

public class GetEarnedFundsForTimeSpanAction implements IAction {
    Scanner scanner = new Scanner(System.in);

    @Override
    public void execute() throws OrderExeption {
        try {
            System.out.println("Введите дату \"c\": ");
            Date dateFrom = new Date(scanner.nextLine());
            System.out.println("Введите дату \"по\": ");
            Date dateTo = new Date(scanner.nextLine());
            System.out.println(BookStore.getEarnedFundsForTimeSpan(dateFrom, dateTo));
        }
        catch (Exception exception) {
            throw new OrderExeption("Введены невалидные даты. Формат ввода даты - дд/мм/гггг. Попробуйте снова.");
        }
    }
}
