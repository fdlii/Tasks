package com.task_8_2.interfaces;

import com.task_3_4.*;
import com.task_6_2.OrderException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface IBookStore {
    void addBook(String name, String author, String description, Date published, double price, int countInStock) throws SQLException;
    void deleteBook(String bookName) throws SQLException;
    List<Book> getBooks() throws SQLException;
    List<Book> getStaledBooks() throws SQLException;
    void addInStock(String bookName, int count) throws SQLException;
    void debitFromStock(String bookName) throws SQLException;

    List<Order> getOrders() throws SQLException;
    Order getOrderById(int id) throws SQLException;
    Order createOrder(double discount, Date executionDate, String clientName, String... bookNames) throws SQLException;
    void cancelOrder(int orderId) throws OrderException, SQLException;
    boolean completeOrder(int orderId) throws OrderException, SQLException;

    List<Client> getClients() throws SQLException;
    void addClient(String name, int age) throws SQLException;

    double getEarnedFundsForTimeSpan(Date from, Date to) throws SQLException;
    int getCompletedOrdersCountForTimeSpan(Date from, Date to) throws SQLException;
    List<Order> getCompletedOrdersForTimeSpan(Date from, Date to) throws SQLException;

    boolean makeRequest(String bookName, int count) throws SQLException;
    List<Request> getBookRequests(String bookName) throws SQLException;
    List<Request> getRequests() throws SQLException;

    void importBooksFromCSVFile(String filename)  throws IOException, SQLException;
    void importOrdersFromCSVFile(String filename)  throws IOException, SQLException;
    void importClientsFromCSVFile(String filename)  throws IOException, SQLException;
    void importRequestsFromCSVFile(String filename)  throws IOException, SQLException;

    void exportBooksIntoCSVFile(String filename)  throws IOException, SQLException;
    void exportOrdersIntoCSVFile(String filename)  throws IOException, SQLException;
    void exportClientsIntoCSVFile(String filename)  throws IOException, SQLException;
    void exportRequestsIntoCSVFile(String filename)  throws IOException, SQLException;

//    void getSerializedObjects()  throws IOException;
}
