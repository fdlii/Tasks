package task_3_4;

import javax.imageio.IIOException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestClass {
    public static void main(String[] args) throws IOException {
        List<Book> books = FileManager.deserializeObjects(Book.class, "books.json");
        System.out.println("dsf");
    }
}
