package task_3_4;

import javax.imageio.IIOException;
import java.io.IOException;
import java.util.Date;

public class TestClass {
    public static void main(String[] args) throws IOException {
        BookStore.importBooksFromCSVFile("task_6_1/Books.csv");
        BookStore.importClientsFromCSVFile("task_6_1/Clients.csv");
        BookStore.importOrdersFromCSVFile("task_6_1/Orders.csv");

        BookStore.ExportBooksIntoCSVFile("task_6_1/OutBooks.csv");
        BookStore.ExportOrdersIntoCSVFile("task_6_1/OutOrders.csv");
        BookStore.ExportClientsIntoCSVFile("task_6_1/OutClients.csv");

    }
}
