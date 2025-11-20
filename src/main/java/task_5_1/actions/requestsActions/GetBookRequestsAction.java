package task_5_1.actions.requestsActions;

import task_5_1.actions.IAction;
import task_3_4.BookStore;
import task_3_4.Request;
import task_6_2.BookExeption;

import java.util.Scanner;

public class GetBookRequestsAction implements IAction {
    Scanner scanner = new Scanner(System.in);

    @Override
    public void execute() throws BookExeption {
        try {
            System.out.println("Введите название книги: ");
            String name = scanner.nextLine();
            System.out.println("Список запросов: ");
            for (Request request : BookStore.getBookRequests(name)) {
                String isOpen;
                if (request.isOpen()) {
                    isOpen = "Да";
                }
                else {
                    isOpen = "Нет";
                }
                System.out.printf("Идентификатор: %d, книга: %s, число книг: %d, открыт: %s.%n",
                        request.getId(),
                        request.getBook().getName(),
                        request.getCount(),
                        isOpen);
            }
        }
        catch (Exception exception) {
            throw new BookExeption("Введены невалидные данные книги. Попробуйте снова.");
        }
    }
}
