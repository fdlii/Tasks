package com.yourcompany.task_5_1.actions.booksActions;

import com.yourcompany.task_5_1.actions.IAction;
import com.yourcompany.task_3_4.interfaces.IBookStore;
import org.hibernate.HibernateException;

import java.io.IOException;
import java.util.Scanner;

public class ImportBooksFromCSVFileAction implements IAction {
    Scanner scanner = new Scanner(System.in);
    IBookStore bookStore;

    public ImportBooksFromCSVFileAction(IBookStore bookStore) {
        this.bookStore = bookStore;
    }

    @Override
    public void execute() throws IOException {
        try {
            System.out.println("Введите путь к файлу для импорта:");
            String fileName = scanner.nextLine();
            bookStore.importBooksFromCSVFile(fileName);
            System.out.println("Книги импортированы.");
        } catch (HibernateException e) {
            System.out.println("Не удалось импортировать книги в БД.");
        }
    }
}
