package com.task_5_1.actions.booksActions;

import com.task_5_1.actions.IAction;
import com.task_3_4.Book;
import com.task_3_4.BookStore;
import com.task_8_2.annotations.Inject;
import com.task_8_2.interfaces.IBookStore;

public class GetBooksAction implements IAction {
    IBookStore bookStore;

    public GetBooksAction(IBookStore bookStore) {
        this.bookStore = bookStore;
    }

    @Override
    public void execute() {
        System.out.println("Список книг:");
        for (Book book : bookStore.getBooks()) {
            System.out.printf("Название: %s, автор: %s, дата публикации: %s, цена: %f, число на складе: %d.%n",
                    book.getName(),
                    book.getAuthor(),
                    book.getPublished(),
                    book.getPrice(),
                    book.getCountInStock());
        }
    }
}
