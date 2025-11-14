package actions.requestsActions;

import actions.IAction;
import task_3_4.BookStore;

import java.io.IOException;
import java.util.Scanner;

public class ImportRequestsFromCSVFileAction implements IAction {
    Scanner scanner = new Scanner(System.in);
    @Override
    public void execute() throws IOException {
        System.out.println("Введите путь к файлу для импорта:");
        String fileName = scanner.nextLine();
        BookStore.importRequestsFromCSVFile(fileName);
        System.out.println("Запросы импортированы.");
    }
}
