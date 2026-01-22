package com.task_5_1.actions.requestsActions;

import com.task_5_1.actions.IAction;
import com.task_6_2.BookException;
import com.task_8_2.interfaces.IBookStore;
import org.hibernate.HibernateException;

import java.util.Scanner;

public class MakeRequestAction implements IAction {
    Scanner scanner = new Scanner(System.in);
    IBookStore bookStore;

    public MakeRequestAction(IBookStore bookStore) {
        this.bookStore = bookStore;
    }

    @Override
    public void execute() throws BookException {
        try {
            System.out.println("Введите название книги: ");
            String name = scanner.nextLine();
            System.out.println("Введите число книг на запрос: ");
            int count = scanner.nextInt();
            scanner.nextLine();
            boolean flag = bookStore.makeRequest(name, count);
        }
        catch (HibernateException exception) {
            System.out.println("Не удалось добавить запрос в БД.");
        }
        catch (Exception exception) {
            scanner.nextLine();
            throw new BookException("Введено невалидное значение книг на запрос. Попробуйте снова.");
        }
    }
}
