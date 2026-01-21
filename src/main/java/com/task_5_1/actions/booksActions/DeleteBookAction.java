package com.task_5_1.actions.booksActions;

import com.task_5_1.actions.IAction;
import com.task_8_2.interfaces.IBookStore;
import org.hibernate.HibernateException;

import java.sql.SQLException;
import java.util.Scanner;

public class DeleteBookAction implements IAction {
    Scanner scanner = new Scanner(System.in);
    IBookStore bookStore;

    public DeleteBookAction(IBookStore bookStore) {
        this.bookStore = bookStore;
    }

    @Override
    public void execute() {
        try {
            System.out.println("Введите название книги: ");
            String name = scanner.nextLine();
            bookStore.deleteBook(name);
        } catch (HibernateException e) {
            System.out.println("Не удалось удалить книгу из БД.");
        }
    }
}
