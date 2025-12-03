package task_5_1.actions.booksActions;

import task_5_1.actions.IAction;
import task_3_4.Book;
import task_3_4.BookStore;

public class GetStaledBooksAction implements IAction {
    @Override
    public void execute() {
        System.out.println("Список залежавшихся книг:");
        for (Book book : BookStore.getStaledBooks()) {
            System.out.printf("Название: %s, автор: %s, дата публикации: %s, цена: %f, число на складе: %d.%n",
                    book.getName(),
                    book.getAuthor(),
                    book.getPublished(),
                    book.getPrice(),
                    book.getCountInStock());
        }
    }
}
