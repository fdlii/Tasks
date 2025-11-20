package task_3_4;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;

@JsonAutoDetect
public class Request {
    private static int counter = 1;

    private int id;
    @JsonIgnore
    private Book book;
    private int count;
    private boolean isOpen;

    public Request(){};

    public Request(Book book, int count) {
        this.id = counter++; // заглушка
        this.book = book;
        this.count = count;
        isOpen = true;
    }

    public int getId() {
        return id;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
