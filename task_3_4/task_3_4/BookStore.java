package task_3_4;

import java.io.*;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import task_4_1.*;

//книжный магазин
public class BookStore {
    private static List<Book> books = new ArrayList<>();
    private static List<Order> orders = new ArrayList<>();
    private static List<Client> clients = new ArrayList<>();
    private static List<Request> requests = new ArrayList<>();

    public static void addBook(String name, String author, String description, Date published, double price, int countInStock) {
        books.add(new Book(name, author, description, published, price, countInStock));
    }

    public static void deleteBook(String bookName) {
        books.stream()
                .filter(book -> book.getName().equals(bookName))
                .forEach(book -> {
                    for (Request r : book.getRequests()) {
                        requests.remove(r);
                    }
                    books.remove(book);
                });
    }

    public static List<Book> getBooks() {
        books.sort(new BookNameComparator()
                .thenComparing(new BookPublishedComparator()
                        .thenComparing(new BookPriceComparator()
                                .thenComparing(new BookIsInStockComparator()))));
        return books;
    }

    public static List<Book> getStaledBooks() {
        List<Book> notStaledBooks = new ArrayList<>();
        for (Order order : orders) {
            if (ChronoUnit.MONTHS.between((new Date()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    order.getExecutionDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()) < 6)
            {
                notStaledBooks.addAll(order.getBooks());
            }
        }

        List<Book> staledBooks = new ArrayList<>();
        for (Book book : books) {
            boolean flag = false;
            for (Book staledBook : staledBooks) {
                if (book.equals(staledBook)) {
                    flag = true;
                }
            }
            if (!flag) {
                staledBooks.add(book);
            }
        }

        staledBooks.sort(new BookPublishedComparator()
                .thenComparing(new BookPriceComparator()));
        return staledBooks;
    }

    public static List<Order> getOrders() {
        orders.sort(new OrderExecutionDateComparator()
                .thenComparing(new OrderFinalPriceComparator()
                        .thenComparing(new OrderStatusComparator())));
        return orders;
    }

    public static Order getOrderById(int id) {
        return orders.stream()
                .filter(order1 -> order1.getId() == id)
                .findFirst()
                .get();
    }

    public static List<Client> getClients() {
        return clients;
    }

    public static void addClient(String name, int age) {
        clients.add(new Client(name, age));
    }

    public static double getEarnedFundsForTimeSpan(Date from, Date to) {
        double sum = 0;
        for (Order order : orders) {
            if
            (
                    order.getExecutionDate().compareTo(from) > 0 &&
                    to.compareTo(order.getExecutionDate()) > 0 &&
                    order.getOrderStatus() == OrderStatus.COMPLETED
            )
            {
                sum += order.getFinalPrice();
            }
        }
        return sum;
    }

    public static int getCompletedOrdersCountForTimeSpan(Date from, Date to) {
        int count = 0;
        for (Order order : orders) {
            if
            (
                    order.getExecutionDate().compareTo(from) > 0 &&
                    to.compareTo(order.getExecutionDate()) > 0 &&
                    order.getOrderStatus() == OrderStatus.COMPLETED
            )
            {
                count += 1;
            }
        }
        return count;
    }

    public static List<Order> getCompletedOrdersForTimeSpan(Date from, Date to) {
        List<Order> validOrders = new ArrayList<>();
        for (Order order : orders) {
            if
            (
                    order.getExecutionDate().compareTo(from) > 0 &&
                    to.compareTo(order.getExecutionDate()) > 0 &&
                    order.getOrderStatus() == OrderStatus.COMPLETED)
            {
                validOrders.add(order);
            }
        }
        validOrders.sort(new OrderExecutionDateComparator()
                .thenComparing(new OrderFinalPriceComparator()));
        return validOrders;
    }

    public static void addInStock(String bookName, int count) {
        for (Book book : books) {
            if (book.getName().equals(bookName)) {
                int sum = book.getCountInStock() + count;
                book.setCountInStock(sum);
                book.setInStock(true);

                for (Request r : book.getRequests()) {
                    if (r.getCount() <= book.getCountInStock() && r.isOpen()) {
                        r.setOpen(false);
                        book.setCountInStock(book.getCountInStock() - r.getCount());
                    }
                }

                if (book.getCountInStock() == 0) {
                    book.setInStock(false);
                }

                break;
            }
        }
    }

    public static void debitFromStock(String bookName) {
        books.stream()
                .filter(book -> book.getName().equals(bookName))
                .forEach(book -> {
                    book.setCountInStock(0);
                    book.setInStock(false);
                });
    }

    public static Order createOrder(double discount, Date executionDate, String clientName, String... bookNames) {
        Order order = null;
        boolean flag = false;
        for (Client client : clients) {
            if (clientName.equals(client.getName())) {
                order = new Order(discount, executionDate, client);
                flag = true;
                break;
            }
        }
        if (!flag)
            return null;
        for (String bookName : bookNames) {
            for (Book book : books) {
                if (book.getName().equals(bookName)) {
                    order.addBook(book);
                    if (book.getCountInStock() == 0) {
                        Request request = new Request(book, 1);
                        book.addRequest(request);
                        requests.add(request);
                    }
                }
            }
        }
        orders.add(order);
        return order;
    }

    public static void cancelOrder(int orderId) {
        orders.stream()
                .filter(order -> order.getId() == orderId)
                .forEach(order -> order.changeStatus(OrderStatus.CANCELLED));
    }

    public static boolean makeRequest(String bookName, int count) {
        for (Book book : books) {
            if (book.getName().equals(bookName)) {
                if (book.getCountInStock() != 0) {
                    return false;
                }
                Request request = new Request(book, count);
                book.addRequest(request);
                requests.add(request);
                return true;
            }
        }
        return false;
    }

    public static boolean completeOrder(int orderId) {
        for (Order order : orders) {
            if (order.getId() == orderId) {
                for (Book book : order.getBooks()) {
                    for (Request request : book.getRequests()) {
                        if (request.isOpen()) {
                            return false;
                        }
                    }
                }
                order.changeStatus(OrderStatus.COMPLETED);
                return true;
            }
        }
        return false;
    }

    public static List<Request> getBookRequests(String bookName) {
        return books.stream()
                .filter(book -> book.getName().equals(bookName))
                .findFirst()
                .get()
                .getRequests();
    }

    public static void importBooksFromCSVFile(String filename) throws IOException {
        FileManager.importBooksFromCSVFile(filename, books, requests);
    }

    public static void importOrdersFromCSVFile(String filename) throws IOException {
        FileManager.importOrdersFromCSVFile(filename, orders, clients, books);
    }

    public static void importClientsFromCSVFile(String filename) throws IOException {
        FileManager.importClientsFromCSVFile(filename, clients, orders);
    }

    public static void importRequestsFromCSVFile(String filename) throws IOException {
        FileManager.importRequestsFromCSVFile(filename, requests, books);
    }

    public static void exportBooksIntoCSVFile(String filename) throws IOException {
        FileManager.exportBooksIntoCSVFile(filename, books);
    }

    public static void exportOrdersIntoCSVFile(String filename) throws IOException {
        FileManager.exportOrdersIntoCSVFile(filename, orders);
    }

    public static void exportClientsIntoCSVFile(String filename) throws IOException {
        FileManager.exportClientsIntoCSVFile(filename, clients);
    }

    public static void exportRequestsIntoCSVFile(String filename) throws IOException {
        FileManager.exportRequestsIntoCSVFile(filename, requests);
    }
}
