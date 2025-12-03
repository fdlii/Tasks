package actions.booksActions;

import actions.IAction;
import task_3_4.BookStore;

import java.io.IOException;
import java.util.Scanner;

public class ImportBooksFromCSVFileAction implements IAction {
    Scanner scanner = new Scanner(System.in);
    @Override
    public void execute() throws IOException {
        System.out.println("Введите путь к файлу для импорта:");
        String fileName = scanner.nextLine();
        BookStore.importBooksFromCSVFile(fileName);
        System.out.println("Книги импортированы.");
    }
}
