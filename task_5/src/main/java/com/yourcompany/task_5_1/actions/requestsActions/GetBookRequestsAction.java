package com.yourcompany.task_5_1.actions.requestsActions;

import com.yourcompany.exceptions.BookException;
import com.yourcompany.models.Request;
import com.yourcompany.task_5_1.actions.IAction;
import com.yourcompany.task_3_4.interfaces.IBookStore;
import org.hibernate.HibernateException;

import java.util.Scanner;

public class GetBookRequestsAction implements IAction {
    Scanner scanner = new Scanner(System.in);
    IBookStore bookStore;

    public GetBookRequestsAction(IBookStore bookStore) {
        this.bookStore = bookStore;
    }

    @Override
    public void execute() throws BookException {
        try {
            System.out.println("Введите название книги: ");
            String name = scanner.nextLine();
            System.out.println("Список запросов: ");
            for (Request request : bookStore.getBookRequests(name)) {
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
        catch (HibernateException exception) {
            System.out.println("Не удалось получить запросы книги.");
        }
        catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new BookException("Введены невалидные данные книги или книги не существует. Попробуйте снова.");
        }
    }
}
