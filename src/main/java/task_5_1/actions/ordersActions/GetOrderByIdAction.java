package task_5_1.actions.ordersActions;

import task_5_1.actions.IAction;
import task_3_4.BookStore;
import task_3_4.Order;
import task_6_2.OrderExeption;

import java.util.Scanner;

public class GetOrderByIdAction implements IAction {
    Scanner scanner = new Scanner(System.in);
    @Override
    public void execute() throws OrderExeption {
        try {
            System.out.println("Введите идентификатор заказа: ");
            int id = scanner.nextInt();
            Order order = BookStore.getOrderById(id);
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
        catch (Exception exception) {
            throw new OrderExeption("Введены невалидные данные заказа. Попробуйте снова.");
        }
    }
}
