package com.task_5_1.actions.ordersActions;

import com.task_5_1.actions.IAction;
import com.task_3_4.BookStore;
import com.task_3_4.Order;
import com.task_6_2.OrderExeption;
import com.task_8_2.annotations.Inject;
import com.task_8_2.interfaces.IBookStore;

import java.util.Date;
import java.util.Scanner;

public class GetCompletedOrdersForTimeSpanAction implements IAction {
    Scanner scanner = new Scanner(System.in);
    IBookStore bookStore;

    public GetCompletedOrdersForTimeSpanAction(IBookStore bookStore) {
        this.bookStore = bookStore;
    }

    @Override
    public void execute() throws OrderExeption {
        try {
            System.out.println("Введите дату \"c\": ");
            Date dateFrom = new Date(scanner.nextLine());
            System.out.println("Введите дату \"по\": ");
            Date dateTo = new Date(scanner.nextLine());
            System.out.println("Сведения о заказах: ");
            for (Order order : bookStore.getCompletedOrdersForTimeSpan(dateFrom, dateTo)) {
                String status = "";
                switch (order.getOrderStatus()) {
                    case NEW : status = "Новый";
                    case COMPLETED: status = "Завершён";
                    case CANCELLED: status = "Отменён";
                }
                System.out.printf("Идентификатор: %d, дата выполнения: %s, имя клиента: %s, статус: %s.%n",
                        order.getId(),
                        order.getExecutionDate(),
                        order.getClient().getName(),
                        status);
            }
        }
        catch (Exception exception) {
            throw new OrderExeption("Введены невалидные даты. Формат ввода даты - дд/мм/гггг. Попробуйте снова.");
        }
    }
}
