package com.task_5_1.actions.ordersActions;

import com.task_5_1.actions.IAction;
import com.task_3_4.Order;
import com.task_6_2.OrderException;
import com.task_8_2.interfaces.IBookStore;

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
            int id = scanner.nextInt();
            scanner.nextLine();
            Order order = bookStore.getOrderById(id);
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
            throw new OrderException("Введены невалидные данные заказа. Попробуйте снова.");
        }
    }
}
