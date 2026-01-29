package com.yourcompany.task_5_1.actions.ordersActions;

import com.yourcompany.exceptions.OrderException;
import com.yourcompany.models.Order;
import com.yourcompany.task_5_1.actions.IAction;
import com.yourcompany.task_3_4.interfaces.IBookStore;
import org.hibernate.HibernateException;

import java.util.Date;
import java.util.Scanner;

public class GetCompletedOrdersForTimeSpanAction implements IAction {
    Scanner scanner = new Scanner(System.in);
    IBookStore bookStore;

    public GetCompletedOrdersForTimeSpanAction(IBookStore bookStore) {
        this.bookStore = bookStore;
    }

    @Override
    public void execute() throws OrderException {
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
        catch (HibernateException exception) {
            System.out.println("Не удалось получить заказы.");
        }
        catch (Exception exception) {
            scanner.nextLine();
            throw new OrderException("Введены невалидные даты. Формат ввода даты - дд/мм/гггг. Попробуйте снова.");
        }
    }
}
