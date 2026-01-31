package com.yourcompany.task_5_1.actions.booksActions;

import com.yourcompany.models.Book;
import com.yourcompany.task_5_1.actions.IAction;
import com.yourcompany.task_3_4.interfaces.IBookStore;
import org.hibernate.HibernateException;

public class GetStaledBooksAction implements IAction {
    IBookStore bookStore;

    public GetStaledBooksAction(IBookStore bookStore) {
        this.bookStore = bookStore;
    }

    @Override
    public void execute() {
        try {
            System.out.println("Список залежавшихся книг:");
            for (Book book : bookStore.getStaledBooks()) {
                System.out.printf("Название: %s, автор: %s, дата публикации: %s, цена: %f, число на складе: %d.%n",
                        book.getName(),
                        book.getAuthor(),
                        book.getPublished(),
                        book.getPrice(),
                        book.getCountInStock());
            }
        } catch (HibernateException e) {
            System.out.println("Ошибка при получении книг из БД.");
        }
    }
}
