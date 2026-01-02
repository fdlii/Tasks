package com.task_5_1.actions.requestsActions;

import com.task_5_1.actions.IAction;
import com.task_3_4.Request;
import com.task_6_2.BookException;
import com.task_8_2.interfaces.IBookStore;

import java.sql.SQLException;
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
        catch (SQLException exception) {
            System.out.println("Не удалось получить запросы книги.");
        }
        catch (Exception exception) {
            throw new BookException("Введены невалидные данные книги или книги не существует. Попробуйте снова.");
        }
    }
}
