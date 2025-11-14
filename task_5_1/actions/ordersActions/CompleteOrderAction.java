package actions.ordersActions;

import actions.IAction;
import task_3_4.BookStore;
import task_6_2.OrderExeption;

import java.util.Scanner;

public class CompleteOrderAction implements IAction {
    Scanner scanner = new Scanner(System.in);

    @Override
    public void execute() throws OrderExeption {
        try {
            System.out.println("Введите идентификатор заказа: ");
            int id = scanner.nextInt();
            BookStore.completeOrder(id);
        }
        catch (Exception exception) {
            throw new OrderExeption("Введены невалидные данные заказа. Попробуйте снова.");
        }
    }
}
