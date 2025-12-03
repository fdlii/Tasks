package com.task_5_1.actions.ordersActions;

import com.task_5_1.actions.IAction;
import com.task_3_4.BookStore;
import com.task_3_4.Order;
import com.task_8_2.annotations.Inject;
import com.task_8_2.interfaces.IBookStore;

public class GetOrdersAction implements IAction {
    IBookStore bookStore;

    public GetOrdersAction(IBookStore bookStore) {
        this.bookStore = bookStore;
    }

    @Override
    public void execute() {
        for (Order order : bookStore.getOrders()) {
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
}
