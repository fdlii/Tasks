package com.yourcompany.task_5_1.actions.ordersActions;

import com.yourcompany.models.Order;
import com.yourcompany.task_5_1.actions.IAction;
import com.yourcompany.task_3_4.interfaces.IBookStore;
import org.hibernate.HibernateException;

public class GetOrdersAction implements IAction {
    IBookStore bookStore;

    public GetOrdersAction(IBookStore bookStore) {
        this.bookStore = bookStore;
    }

    @Override
    public void execute() {
        try {
            for (Order order : bookStore.getOrders()) {
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
        } catch (HibernateException e) {
            System.out.println("Ошибка при получении заказов из БД.");
        }
    }
}
