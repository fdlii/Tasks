package com.task_5_1.actions.requestsActions;

import com.task_5_1.actions.IAction;
import com.task_3_4.BookStore;
import com.task_8_2.annotations.Inject;
import com.task_8_2.interfaces.IBookStore;

import java.io.IOException;
import java.util.Scanner;

public class ImportRequestsFromCSVFileAction implements IAction {
    Scanner scanner = new Scanner(System.in);
    IBookStore bookStore;

    public ImportRequestsFromCSVFileAction(IBookStore bookStore) {
        this.bookStore = bookStore;
    }

    @Override
    public void execute() throws IOException {
        System.out.println("Введите путь к файлу для импорта:");
        String fileName = scanner.nextLine();
        bookStore.importRequestsFromCSVFile(fileName);
        System.out.println("Запросы импортированы.");
    }
}
