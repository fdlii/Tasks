package task_3_4;

import java.util.Random;

public class Request {
    private Random random = new Random();
    private int id;
    private Book book;
    private int count;
    private boolean isOpen;

    public Request(Book book, int count) {
        this.id = random.nextInt(10000); // заглушка
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
