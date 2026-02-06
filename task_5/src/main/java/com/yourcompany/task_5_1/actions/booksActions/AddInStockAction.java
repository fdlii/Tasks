package com.yourcompany.task_5_1.actions.booksActions;

import com.yourcompany.exceptions.BookException;
import com.yourcompany.task_5_1.actions.IAction;
import com.yourcompany.task_3_4.interfaces.IBookStore;
import org.hibernate.HibernateException;

import java.util.Scanner;

public class AddInStockAction implements IAction {
    Scanner scanner = new Scanner(System.in);
    IBookStore bookStore;

    public AddInStockAction(IBookStore bookStore) {
        this.bookStore = bookStore;
    }

    @Override
    public void execute() throws BookException {
        try {
            System.out.println("Введите название книги: ");
            String name = scanner.nextLine();
            System.out.println("Введите число книг для добавления: ");
            int count = scanner.nextInt();
            scanner.nextLine();
            bookStore.addInStock(name, count);
        }
        catch (HibernateException exception) {
            System.out.println("Не удалось добавить книгу на склад.");
        }
        catch (Exception exception) {
            scanner.nextLine();
            throw new BookException("Введены невалидные данные книги. Попробуйте снова.");
        }
    }
}
