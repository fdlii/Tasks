package com.task_5_1.actions.ordersActions;

import com.task_5_1.actions.IAction;
import com.task_3_4.BookStore;
import com.task_8_2.annotations.Inject;
import com.task_8_2.interfaces.IBookStore;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class ExportOrdersFromCSVFileAction implements IAction {
    Scanner scanner = new Scanner(System.in);
    IBookStore bookStore;

    public ExportOrdersFromCSVFileAction(IBookStore bookStore) {
        this.bookStore = bookStore;
    }

    @Override
    public void execute() throws IOException {
        try {
            System.out.println("Введите путь к файлу для экспорта:");
            String fileName = scanner.nextLine();
            bookStore.exportOrdersIntoCSVFile(fileName);
            System.out.println("Заказы экспортированы.");
        } catch (SQLException e) {
            System.out.println("Не удалось экспортировать заказы из БД.");
        }
    }
}
