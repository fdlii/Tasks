package com.yourcompany.task_5_1.actions.booksActions;

import com.yourcompany.exceptions.BookException;
import com.yourcompany.task_5_1.actions.IAction;
import com.yourcompany.task_3_4.interfaces.IBookStore;
import org.hibernate.HibernateException;

import java.util.Scanner;

public class DebitFromStockAction implements IAction {
    Scanner scanner = new Scanner(System.in);
    IBookStore bookStore;

    public DebitFromStockAction(IBookStore bookStore) {
        this.bookStore = bookStore;
    }

    @Override
    public void execute() throws BookException {
        try {
            System.out.println("Введите название книги: ");
            String name = scanner.nextLine();
            bookStore.debitFromStock(name);
        }
        catch (HibernateException exception) {
            System.out.println("Не удалось удалить книгу со склада.");
        }
        catch (Exception exception) {
            throw new BookException("Введены невалидные данные книги. Попробуйте снова.");
        }
    }
}
