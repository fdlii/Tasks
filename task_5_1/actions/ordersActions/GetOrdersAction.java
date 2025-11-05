package actions.ordersActions;

import actions.IAction;
import task_3_4.BookStore;
import task_3_4.Order;

public class GetOrdersAction implements IAction {
    @Override
    public void execute() {
        for (Order order : BookStore.getOrders()) {
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
