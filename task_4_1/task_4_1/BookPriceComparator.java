package task_4_1;

import task_3_4.Book;

import java.util.Comparator;

public class BookPriceComparator implements Comparator<Book> {
    @Override
    public int compare(Book book1, Book book2) {
        double diff = book1.getPrice() - book2.getPrice();

        if (diff < 0) {
            return -1;
        } else if (diff > 0){
            return 1;
        } else {
            return 0;
        }

    }
}
