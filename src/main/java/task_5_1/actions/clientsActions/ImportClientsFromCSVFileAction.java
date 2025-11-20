package task_5_1.actions.clientsActions;

import task_5_1.actions.IAction;
import task_3_4.BookStore;

import java.io.IOException;
import java.util.Scanner;

public class ImportClientsFromCSVFileAction implements IAction {
    Scanner scanner = new Scanner(System.in);
    @Override
    public void execute() throws IOException {
        System.out.println("Введите путь к файлу для импорта:");
        String fileName = scanner.nextLine();
        BookStore.exportClientsIntoCSVFile(fileName);
        System.out.println("Клиенты импортированы.");
    }
}
