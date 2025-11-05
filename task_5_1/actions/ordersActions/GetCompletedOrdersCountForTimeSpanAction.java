package actions.ordersActions;

import actions.IAction;
import task_3_4.BookStore;

import java.util.Date;
import java.util.Scanner;

public class GetCompletedOrdersCountForTimeSpanAction implements IAction {
    Scanner scanner = new Scanner(System.in);
    @Override
    public void execute() {
        System.out.println("Введите дату \"c\": ");
        Date dateFrom = new Date(scanner.nextLine());
        System.out.println("Введите дату \"по\": ");
        Date dateTo = new Date(scanner.nextLine());
        System.out.println("Число заказов: " + BookStore.getCompletedOrdersCountForTimeSpan(dateFrom, dateTo));
    }
}
