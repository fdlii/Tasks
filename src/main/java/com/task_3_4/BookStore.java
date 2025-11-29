package com.task_3_4;

import com.task_4_1.*;
import com.task_8_1.Configuration;
import com.task_8_2.annotations.Inject;
import com.task_8_2.annotations.PostConstruct;
import com.task_8_2.interfaces.IBookStore;
import com.task_8_2.interfaces.IConfigurator;

import java.io.*;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//книжный магазин
public class BookStore implements IBookStore {
    private static List<Book> books = new ArrayList<>();
    private static List<Order> orders = new ArrayList<>();
    private static List<Client> clients = new ArrayList<>();
    private static List<Request> requests = new ArrayList<>();
    private static Configuration configuration;
    @Inject
    private IConfigurator configurator;
    @Inject
    private FileManager fileManager;

    public BookStore() {}

    @PostConstruct
    private void setConfiguration(){
        configuration = configurator.getConfiguration();
    }

    @Override
    public void addBook(String name, String author, String description, Date published, double price, int countInStock) {
        books.add(new Book(name, author, description, published, price, countInStock));
    }

    @Override
    public void deleteBook(String bookName) {
        books.stream()
                .filter(book -> book.getName().equals(bookName))
                .forEach(book -> {
                    for (Request r : book.getRequests()) {
                        requests.remove(r);
                    }
                    books.remove(book);
                });
    }

    @Override
    public List<Book> getBooks() {
        books.sort(new BookNameComparator()
                .thenComparing(new BookPublishedComparator()
                        .thenComparing(new BookPriceComparator()
                                .thenComparing(new BookIsInStockComparator()))));
        return books;
    }

    @Override
    public List<Book> getStaledBooks() {
        List<Book> notStaledBooks = new ArrayList<>();
        for (Order order : orders) {
            if (ChronoUnit.MONTHS.between((new Date()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    order.getExecutionDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()) < configuration.staledBooksDeadline)
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

    @Override
    public List<Order> getOrders() {
        orders.sort(new OrderExecutionDateComparator()
                .thenComparing(new OrderFinalPriceComparator()
                        .thenComparing(new OrderStatusComparator())));
        return orders;
    }

    @Override
    public Order getOrderById(int id) {
        return orders.stream()
                .filter(order1 -> order1.getId() == id)
                .findFirst()
                .get();
    }

    @Override
    public List<Client> getClients() {
        return clients;
    }

    @Override
    public void addClient(String name, int age) {
        clients.add(new Client(name, age));
    }

    @Override
    public double getEarnedFundsForTimeSpan(Date from, Date to) {
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

    @Override
    public int getCompletedOrdersCountForTimeSpan(Date from, Date to) {
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

    @Override
    public List<Order> getCompletedOrdersForTimeSpan(Date from, Date to) {
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

    @Override
    public void addInStock(String bookName, int count) {
        for (Book book : books) {
            if (book.getName().equals(bookName)) {
                int sum = book.getCountInStock() + count;
                book.setCountInStock(sum);
                book.setInStock(true);

                if (configuration.canCompleteRequest) {
                    for (Request r : book.getRequests()) {
                        if (r.getCount() <= book.getCountInStock() && r.isOpen()) {
                            r.setOpen(false);
                            book.setCountInStock(book.getCountInStock() - r.getCount());
                        }
                    }

                    if (book.getCountInStock() == 0) {
                        book.setInStock(false);
                    }
                }
                break;
            }
        }
    }

    @Override
    public void debitFromStock(String bookName) {
        books.stream()
                .filter(book -> book.getName().equals(bookName))
                .forEach(book -> {
                    book.setCountInStock(0);
                    book.setInStock(false);
                });
    }

    @Override
    public Order createOrder(double discount, Date executionDate, String clientName, String... bookNames) {
        Order order = null;
        boolean flag = false;
        for (Client client : clients) {
            if (clientName.equals(client.getName())) {
                order = new Order(discount, executionDate, client);
                client.addOrder(order);
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

    @Override
    public void cancelOrder(int orderId) {
        orders.stream()
                .filter(order -> order.getId() == orderId)
                .forEach(order -> order.changeStatus(OrderStatus.CANCELLED));
    }

    @Override
    public boolean makeRequest(String bookName, int count) {
        for (Book book : books) {
            if (book.getName().equals(bookName)) {
                Request request = new Request(book, count);
                book.addRequest(request);
                requests.add(request);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean completeOrder(int orderId) {
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

    @Override
    public List<Request> getBookRequests(String bookName) {
        return books.stream()
                .filter(book -> book.getName().equals(bookName))
                .findFirst()
                .get()
                .getRequests();
    }

    @Override
    public List<Request> getRequests() {
        return requests;
    }

    @Override
    public void importBooksFromCSVFile(String filename) throws IOException {
        fileManager.importBooksFromCSVFile(filename, books, requests);
    }

    @Override
    public void importOrdersFromCSVFile(String filename) throws IOException {
        fileManager.importOrdersFromCSVFile(filename, orders, clients, books);
    }

    @Override
    public void importClientsFromCSVFile(String filename) throws IOException {
        fileManager.importClientsFromCSVFile(filename, clients, orders);
    }

    @Override
    public void importRequestsFromCSVFile(String filename) throws IOException {
        fileManager.importRequestsFromCSVFile(filename, requests, books);
    }

    @Override
    public void exportBooksIntoCSVFile(String filename) throws IOException {
        fileManager.exportBooksIntoCSVFile(filename, books);
    }

    @Override
    public void exportOrdersIntoCSVFile(String filename) throws IOException {
        fileManager.exportOrdersIntoCSVFile(filename, orders);
    }

    @Override
    public void exportClientsIntoCSVFile(String filename) throws IOException {
        fileManager.exportClientsIntoCSVFile(filename, clients);
    }

    @Override
    public void exportRequestsIntoCSVFile(String filename) throws IOException {
        fileManager.exportRequestsIntoCSVFile(filename, requests);
    }

    @Override
    public void getSerializedObjects() throws IOException {
        requests = fileManager.deserializeObjects(Request.class, "requests.json");
        books = fileManager.deserializeObjects(Book.class, "books.json");
        orders = fileManager.deserializeObjects(Order.class, "orders.json");
        clients = fileManager.deserializeObjects(Client.class, "clients.json");


        for (Book book : books) {
            for (Request request1 : book.getRequests()) {
                for (Request request2 : requests) {
                    if (request1.getId() == request2.getId()) {
                        book.deleteRequest(request1);
                        book.addRequest(request2);
                    }
                }
            }
        }

        for (Order order : orders) {
            for (Book book1 : order.getBooks()) {
                for (Book book2 : books) {
                    if (book1.getId() == book2.getId()) {
                        order.deleteBook(book1);
                        order.addBook(book2);
                    }
                }
            }
        }

        for (Client client : clients) {
            for (Order order1 : client.getOrders()) {
                for (Order order2 : orders) {
                    if (order1.getId() == order2.getId()) {
                        client.deleteOrder(order1);
                        client.addOrder(order2);
                    }
                }
            }
        }

        for (Book book : books) {
            for (Request request : book.getRequests()) {
                request.setBook(book);
            }
        }

        for (Client client : clients) {
            for (Order order : client.getOrders()) {
                order.setClient(client);
            }
        }
    }
}
