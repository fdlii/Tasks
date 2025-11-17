package actions.ordersActions;

import actions.IAction;
import task_3_4.BookStore;

import java.util.Scanner;

public class CompleteOrderAction implements IAction {
    Scanner scanner = new Scanner(System.in);

    @Override
    public void execute() {
        System.out.println("Введите идентификатор заказа: ");
        int id = scanner.nextInt();
        BookStore.completeOrder(id);
    }
}
