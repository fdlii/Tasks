package com.task_5_1.actions.requestsActions;

import com.task_5_1.actions.IAction;
import com.task_3_4.BookStore;
import com.task_8_2.annotations.Inject;
import com.task_8_2.interfaces.IBookStore;

import java.io.IOException;
import java.util.Scanner;

public class ExportRequestsFromCSVFileAction implements IAction {
    Scanner scanner = new Scanner(System.in);
    IBookStore bookStore;

    public ExportRequestsFromCSVFileAction(IBookStore bookStore) {
        this.bookStore = bookStore;
    }

    @Override
    public void execute() throws IOException {
        System.out.println("Введите путь к файлу для экспорта:");
        String fileName = scanner.nextLine();
        bookStore.exportRequestsIntoCSVFile(fileName);
        System.out.println("Запросы экспортированы.");
    }
}
