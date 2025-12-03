package task_5_1.actions.booksActions;

import task_5_1.actions.IAction;
import task_3_4.BookStore;
import task_6_2.BookExeption;
import java.util.Date;
import java.util.Scanner;

public class AddBookAction implements IAction {
    Scanner scanner = new Scanner(System.in);

    @Override
    public void execute() throws BookExeption {
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
            System.out.println("Введите число книг на складе: ");
            int count = scanner.nextInt();
            BookStore.addBook(name, author, description, date, price, count);
        }
        catch (Exception exception) {
            throw new BookExeption("Введены невалидные данные книги. Попробуйте снова.");
        }
    }
}
