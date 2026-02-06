package com.yourcompany.task_5_1.actions.clientsActions;

import com.yourcompany.task_5_1.actions.IAction;
import com.yourcompany.task_3_4.interfaces.IBookStore;
import org.hibernate.HibernateException;

import java.io.IOException;
import java.util.Scanner;

public class ExportClientsFromCSVFileAction implements IAction {
    Scanner scanner = new Scanner(System.in);
    IBookStore bookStore;

    public ExportClientsFromCSVFileAction(IBookStore bookStore) {
        this.bookStore = bookStore;
    }

    @Override
    public void execute() throws IOException {
        try {
            System.out.println("Введите путь к файлу для экспорта:");
            String fileName = scanner.nextLine();
            bookStore.exportClientsIntoCSVFile(fileName);
            System.out.println("Клиенты экспортированы.");
        } catch (HibernateException e) {
            System.out.println("Не удалось экспортировать клиентов из БД.");
        }
    }
}
