package task_4_1;

import task_3_4.Book;

import java.util.Comparator;

public class BookPublishedComparator implements Comparator<Book> {
    @Override
    public int compare(Book book1, Book book2) {
        return book1.getPublished().compareTo(book2.getPublished());
    }
}
