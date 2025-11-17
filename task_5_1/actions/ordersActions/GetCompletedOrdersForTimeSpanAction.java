package actions.ordersActions;

import actions.IAction;
import task_3_4.BookStore;
import task_3_4.Order;
import task_3_4.OrderStatus;

import java.util.Date;
import java.util.Scanner;

public class GetCompletedOrdersForTimeSpanAction implements IAction {
    Scanner scanner = new Scanner(System.in);
    @Override
    public void execute() {
        System.out.println("Введите дату \"c\": ");
        Date dateFrom = new Date(scanner.nextLine());
        System.out.println("Введите дату \"по\": ");
        Date dateTo = new Date(scanner.nextLine());
        System.out.println("Сведения о заказах: ");
        for (Order order : BookStore.getCompletedOrdersForTimeSpan(dateFrom, dateTo)) {
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
