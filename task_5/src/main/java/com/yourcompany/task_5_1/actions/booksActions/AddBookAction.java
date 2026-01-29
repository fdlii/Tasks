package com.yourcompany.task_5_1.actions.booksActions;

import com.yourcompany.task_5_1.actions.IAction;
import com.yourcompany.task_3_4.interfaces.IBookStore;
import org.hibernate.HibernateException;

import java.util.Date;
import java.util.Scanner;

public class AddBookAction implements IAction {
    Scanner scanner = new Scanner(System.in);
    IBookStore bookStore;

    public AddBookAction(IBookStore bookStore) {
        this.bookStore = bookStore;
    }

    @Override
    public void execute() {
        try {
            System.out.println("Введите название книги: ");
            String name = scanner.nextLine();
            System.out.println("Введите автора: ");
            String author = scanner.nextLine();
            System.out.println("Введите описание: ");
            String description = scanner.nextLine();
            System.out.println("Введите дату публикации: ");
            Date date = new Date(scanner.nextLine());
            System.out.println("Введите цену: ");
            double price = scanner.nextDouble();
            scanner.nextLine();
            System.out.println("Введите число книг на складе: ");
            int count = scanner.nextInt();
            scanner.nextLine();
            bookStore.addBook(name, author, description, date, price, count);
        } catch (HibernateException e) {
            System.out.println("Не удалось добавить книгу в БД.");
        }
    }
}
