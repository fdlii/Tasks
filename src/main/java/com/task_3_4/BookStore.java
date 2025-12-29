package com.task_3_4;

import com.task_11_3.interfaces.IBookDAO;
import com.task_11_3.interfaces.IClientDAO;
import com.task_11_3.interfaces.IOrderDAO;
import com.task_11_3.interfaces.IRequestDAO;
import com.task_4_1.*;
import com.task_6_2.BookException;
import com.task_6_2.OrderException;
import com.task_8_2.annotations.Inject;
import com.task_8_2.interfaces.IBookStore;
import com.task_8_2.interfaces.IFileManager;

import java.io.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//книжный магазин
public class BookStore implements IBookStore {
    @Inject
    private IBookDAO bookDAO;
    @Inject
    private IOrderDAO orderDAO;
    @Inject
    private IClientDAO clientDAO;
    @Inject
    private IRequestDAO requestDAO;
    @Inject
    private IFileManager fileManager;

    public BookStore() {}

    @Override
    public void addBook(String name, String author, String description, Date published, double price, int countInStock) {
        bookDAO.createBook(new Book(name, author, description, published, price, countInStock));
    }

    @Override
    public void deleteBook(String bookName) {
        bookDAO.deleteBook(bookName);
    }

    @Override
    public List<Book> getBooks() {
        List<Book> books;
        books = bookDAO.findAll();
        books.sort(new BookNameComparator()
                .thenComparing(new BookPublishedComparator()
                        .thenComparing(new BookPriceComparator()
                                .thenComparing(new BookIsInStockComparator()))));
        return books;
    }

    @Override
    public List<Book> getStaledBooks() {
        List<Book> staledBooks;
        staledBooks = orderDAO.getStaledBooks(Timestamp.valueOf(LocalDateTime.now()));
        staledBooks.sort(new BookPublishedComparator()
                .thenComparing(new BookPriceComparator()));
        return staledBooks;
    }

    @Override
    public List<Order> getOrders() {
        List<Order> orders = orderDAO.findAll();
        orders.sort(new OrderExecutionDateComparator()
                .thenComparing(new OrderFinalPriceComparator()
                        .thenComparing(new OrderStatusComparator())));
        return orders;
    }

    @Override
    public Order getOrderById(int id) {
        return orderDAO.findById(id);
    }

    @Override
    public List<Client> getClients() {
        return clientDAO.findAll();
    }

    @Override
    public void addClient(String name, int age) {
        clientDAO.createClient(new Client(name, age));
    }

    @Override
    public double getEarnedFundsForTimeSpan(Date from, Date to) {
        return orderDAO.getEarnedFundsForTimeSpan(from, to);
    }

    @Override
    public int getCompletedOrdersCountForTimeSpan(Date from, Date to) {
        return orderDAO.getCompletedOrdersCountForTimeSpan(from, to);
    }

    @Override
    public List<Order> getCompletedOrdersForTimeSpan(Date from, Date to) {
        return orderDAO.getCompletedOrdersForTimeSpan(from, to);
    }

    @Override
    public void addInStock(String bookName, int count) {
        boolean isAdded = bookDAO.addBookInStock(bookName, count);
        if (!isAdded) {
            System.out.println("Книги с таким именем не существует.");
        }
    }

    @Override
    public void debitFromStock(String bookName) {
        boolean isDebit = bookDAO.debitBookFromStock(bookName);
        if (!isDebit) {
            System.out.println("Книги с таким именем не существует.");
        }
    }

    @Override
    public Order createOrder(double discount, Date executionDate, String clientName, String... bookNames) {
        List<Client> clients = clientDAO.findAll();

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

        List<Book> books = bookDAO.findAll();

        for (String bookName : bookNames) {
            for (Book book : books) {
                if (book.getName().equals(bookName)) {
                    order.addBook(book);
                    if (book.getCountInStock() == 0) {
                        Request request = new Request(book, 1);
                        book.addRequest(request);
                        requestDAO.createRequest(request);
                    }
                }
            }
        }
        orderDAO.createOrder(order);
        return order;
    }

    @Override
    public void cancelOrder(int orderId) throws OrderException {
        for (Order order : orderDAO.findAll()) {
            if (order.getId() == orderId) {
                order.changeStatus(OrderStatus.CANCELLED);
                orderDAO.updateOrder(order);
                return;
            }
        }
    }

    @Override
    public boolean makeRequest(String bookName, int count) {
        List<Book> books = bookDAO.findAll();

        for (Book book : books) {
            if (book.getName().equals(bookName)) {
                Request request = new Request(book, count);
                book.addRequest(request);
                requestDAO.createRequest(request);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean completeOrder(int orderId) throws OrderException {
        for (Order order : orderDAO.findAll()) {
            if (order.getId() == orderId) {
                order.changeStatus(OrderStatus.COMPLETED);
                orderDAO.updateOrder(order);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Request> getBookRequests(String bookName) {
        return bookDAO.getBookRequests(bookName);
    }

    @Override
    public List<Request> getRequests() {
        return requestDAO.findAll();
    }

    @Override
    public void importBooksFromCSVFile(String filename) throws IOException {
        List<Book> books = new ArrayList<>();
        fileManager.importBooksFromCSVFile(filename, books);
        for (Book book : books) {
            bookDAO.createBook(book);
        }
    }

    @Override
    public void importOrdersFromCSVFile(String filename) throws IOException {
        List<Book> books = bookDAO.findAll();
        List<Client> clients = clientDAO.findAll();
        List<Order> orders = new ArrayList<>();
        fileManager.importOrdersFromCSVFile(filename, orders, clients, books);
        for (Order order : orders) {
            orderDAO.createOrder(order);
        }
    }

    @Override
    public void importClientsFromCSVFile(String filename) throws IOException {
        List<Client> clients = new ArrayList<>();
        fileManager.importClientsFromCSVFile(filename, clients);
        for (Client client : clients) {
            clientDAO.createClient(client);
        }
    }

    @Override
    public void importRequestsFromCSVFile(String filename) throws IOException {
        List<Book> books = bookDAO.findAll();
        List<Request> requests = new ArrayList<>();
        fileManager.importRequestsFromCSVFile(filename, requests, books);
        for (Request request : requests) {
            requestDAO.createRequest(request);
        }
    }

    @Override
    public void exportBooksIntoCSVFile(String filename) throws IOException {
        fileManager.exportBooksIntoCSVFile(filename, bookDAO.findAll());
    }

    @Override
    public void exportOrdersIntoCSVFile(String filename) throws IOException {
        fileManager.exportOrdersIntoCSVFile(filename, orderDAO.findAll());
    }

    @Override
    public void exportClientsIntoCSVFile(String filename) throws IOException {
        fileManager.exportClientsIntoCSVFile(filename, clientDAO.findAll());
    }

    @Override
    public void exportRequestsIntoCSVFile(String filename) throws IOException {
        fileManager.exportRequestsIntoCSVFile(filename, requestDAO.findAll());
    }

//    @Override
//    public void getSerializedObjects() throws IOException {
//        requests = fileManager.deserializeObjects(Request.class, "requests.json");
//        books = fileManager.deserializeObjects(Book.class, "books.json");
//        orders = fileManager.deserializeObjects(Order.class, "orders.json");
//        clients = fileManager.deserializeObjects(Client.class, "clients.json");
//
//
//        for (Book book : books) {
//            List<Request> requests1Copy = new ArrayList<>(book.getRequests());
//            for (Request request1 : requests1Copy) {
//                for (Request request2 : requests) {
//                    if (request1.getId() == request2.getId()) {
//                        book.deleteRequest(request1);
//                        book.addRequest(request2);
//                    }
//                }
//            }
//        }
//
//        for (Order order : orders) {
//            List<Book> books1Copy = new ArrayList<>(order.getBooks());
//            for (Book book1 : books1Copy) {
//                for (Book book2 : books) {
//                    if (book1.getId() == book2.getId()) {
//                        order.deleteBook(book1);
//                        order.addBook(book2);
//                    }
//                }
//            }
//        }
//
//        for (Client client : clients) {
//            List<Order> orders1Copy = new ArrayList<>(client.getOrders());
//            for (Order order1 : orders1Copy) {
//                for (Order order2 : orders) {
//                    if (order1.getId() == order2.getId()) {
//                        client.deleteOrder(order1);
//                        client.addOrder(order2);
//                    }
//                }
//            }
//        }
//
//        for (Book book : books) {
//            for (Request request : book.getRequests()) {
//                request.setBook(book);
//            }
//        }
//
//        for (Client client : clients) {
//            for (Order order : client.getOrders()) {
//                order.setClient(client);
//            }
//        }
//    }
}
