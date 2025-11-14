package task_3_4;

import javax.imageio.IIOException;
import java.io.IOException;
import java.util.Date;

public class TestClass {
    public static void main(String[] args) throws IOException {
        BookStore.importBooksFromCSVFile("task_6_1/Books.csv");
        BookStore.importRequestsFromCSVFile("task_6_1/Requests.csv");
        BookStore.importClientsFromCSVFile("task_6_1/Clients.csv");
        BookStore.importOrdersFromCSVFile("task_6_1/Orders.csv");

        BookStore.exportBooksIntoCSVFile("task_6_1/OutBooks.csv");
        BookStore.exportRequestsIntoCSVFile("task_6_1/OutRequests.csv");
        BookStore.exportOrdersIntoCSVFile("task_6_1/OutOrders.csv");
        BookStore.exportClientsIntoCSVFile("task_6_1/OutClients.csv");

    }
}
