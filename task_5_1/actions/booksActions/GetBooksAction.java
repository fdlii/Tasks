package actions.booksActions;

import actions.IAction;
import task_3_4.Book;
import task_3_4.BookStore;

import java.util.Scanner;

public class GetBooksAction implements IAction {
    @Override
    public void execute() {
        System.out.println("Список книг:");
        for (Book book : BookStore.getBooks()) {
            System.out.printf("Название: %s, автор: %s, дата публикации: %s, цена: %f, число на складе: %d.%n",
                    book.getName(),
                    book.getAuthor(),
                    book.getPublished(),
                    book.getPrice(),
                    book.getCountInStock());
        }
    }
}
