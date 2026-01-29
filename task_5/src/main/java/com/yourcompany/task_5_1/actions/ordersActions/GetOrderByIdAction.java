package com.yourcompany.task_5_1.actions.ordersActions;

import com.yourcompany.exceptions.OrderException;
import com.yourcompany.models.Order;
import com.yourcompany.task_5_1.actions.IAction;
import com.yourcompany.task_3_4.interfaces.IBookStore;
import org.hibernate.HibernateException;

import java.util.Scanner;

public class GetOrderByIdAction implements IAction {
    Scanner scanner = new Scanner(System.in);
    IBookStore bookStore;

    public GetOrderByIdAction(IBookStore bookStore) {
        this.bookStore = bookStore;
    }

    @Override
    public void execute() throws OrderException {
        try {
            System.out.println("Введите идентификатор заказа: ");
            long id = scanner.nextLong();
            scanner.nextLine();
            Order order = bookStore.getOrderById(id);
            String status = switch (order.getOrderStatus()) {
                case NEW -> "Новый";
                case COMPLETED -> "Завершён";
                case CANCELLED -> "Отменён";
            };
            System.out.printf("Идентификатор: %d, дата выполнения: %s, имя клиента: %s, статус: %s.%n",
                    order.getId(),
                    order.getExecutionDate(),
                    order.getClient().getName(),
                    status);
        }
        catch (HibernateException exception) {
            System.out.println("Не удалось получить заказ из БД.");
        }
        catch (Exception exception) {
            scanner.nextLine();
            throw new OrderException("Введены невалидные данные заказа. Попробуйте снова.");
        }
    }
}
