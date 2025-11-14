package actions.ordersActions;

import actions.IAction;
import task_3_4.BookStore;
import task_6_2.OrderExeption;

import java.util.Date;
import java.util.Scanner;

public class CreateOrderAction implements IAction {
    Scanner scanner = new Scanner(System.in);
    @Override
    public void execute() throws OrderExeption {
        try {
            System.out.println("Введите скидку в заказе клиента: ");
            double discount = scanner.nextDouble();
            System.out.println("Введите дату исполнения: ");
            Date date = new Date(scanner.nextLine());
            System.out.println("Введите ФИО клиента: ");
            String name = scanner.nextLine();
            System.out.println("Введите количество книг в заказе клиента: ");
            int count = scanner.nextInt();
            int oldCount = count;
            String bookNames[] = new String[count];
            while (count > 0) {
                System.out.println(String.format("Введите название %d книги: ", oldCount - count + 1));
                bookNames[oldCount - count] = scanner.nextLine();
                count--;
            }
            BookStore.createOrder(discount, date, name, bookNames);
        }
        catch (Exception exception) {
            throw new OrderExeption("Введены невалидные данные заказа. Попробуйте снова.");
        }
    }
}
