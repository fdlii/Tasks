package com.task_4_1;

import com.task_3_4.Book;

import java.util.Comparator;

public class BookIsInStockComparator implements Comparator<Book> {
    @Override
    public int compare(Book book1, Book book2) {
        if (book1.isInStock() == book2.isInStock()) {
            return 0;
        } else if (book1.isInStock()) {
            return 1;
        } else {
            return 0;
        }
    }
}
