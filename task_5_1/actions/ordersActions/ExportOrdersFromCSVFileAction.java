package actions.ordersActions;

import actions.IAction;
import task_3_4.BookStore;

import java.io.IOException;
import java.util.Scanner;

public class ExportOrdersFromCSVFileAction implements IAction {
    Scanner scanner = new Scanner(System.in);
    @Override
    public void execute() throws IOException {
        System.out.println("Введите путь к файлу для экспорта:");
        String fileName = scanner.nextLine();
        BookStore.exportOrdersIntoCSVFile(fileName);
        System.out.println("Заказы экспортированы.");
    }
}
