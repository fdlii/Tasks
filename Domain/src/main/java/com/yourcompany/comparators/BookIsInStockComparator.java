package com.yourcompany.comparators;


import com.yourcompany.models.Book;

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
