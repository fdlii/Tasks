package com.task_8_2.interfaces;

import com.task_3_4.*;
import com.task_6_2.OrderException;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface IBookStore {
    void addBook(String name, String author, String description, Date published, double price, int countInStock);
    void deleteBook(String bookName);
    List<Book> getBooks();
    List<Book> getStaledBooks();
    void addInStock(String bookName, int count);
    void debitFromStock(String bookName);

    List<Order> getOrders();
    Order getOrderById(int id);
    Order createOrder(double discount, Date executionDate, String clientName, String... bookNames);
    void cancelOrder(int orderId) throws OrderException;
    boolean completeOrder(int orderId) throws OrderException;

    List<Client> getClients();
    void addClient(String name, int age);

    double getEarnedFundsForTimeSpan(Date from, Date to);
    int getCompletedOrdersCountForTimeSpan(Date from, Date to);
    List<Order> getCompletedOrdersForTimeSpan(Date from, Date to);

    boolean makeRequest(String bookName, int count);
    List<Request> getBookRequests(String bookName);
    List<Request> getRequests();

    void importBooksFromCSVFile(String filename)  throws IOException;
    void importOrdersFromCSVFile(String filename)  throws IOException;
    void importClientsFromCSVFile(String filename)  throws IOException;
    void importRequestsFromCSVFile(String filename)  throws IOException;

    void exportBooksIntoCSVFile(String filename)  throws IOException;
    void exportOrdersIntoCSVFile(String filename)  throws IOException;
    void exportClientsIntoCSVFile(String filename)  throws IOException;
    void exportRequestsIntoCSVFile(String filename)  throws IOException;

//    void getSerializedObjects()  throws IOException;
}
