package actions.requestsActions;

import actions.IAction;
import task_3_4.BookStore;
import task_3_4.Request;

import java.util.Scanner;

public class GetBookRequestsAction implements IAction {
    Scanner scanner = new Scanner(System.in);

    @Override
    public void execute() {
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
}
