package task_5_1.actions.ordersActions;

import task_5_1.actions.IAction;
import task_3_4.BookStore;
import task_6_2.OrderExeption;

import java.util.Scanner;

public class CancelOrderAction implements IAction {
    Scanner scanner = new Scanner(System.in);

    @Override
    public void execute() throws OrderExeption {
        try {
            System.out.println("Введите идентификатор заказа: ");
            int id = scanner.nextInt();
            BookStore.cancelOrder(id);
        }
        catch (Exception exception) {
            throw new OrderExeption("Введены невалидные данные заказа. Попробуйте снова.");
        }
    }
}
