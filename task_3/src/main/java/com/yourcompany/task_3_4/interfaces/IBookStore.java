package com.yourcompany.task_3_4.interfaces;

import com.yourcompany.exceptions.OrderException;
import com.yourcompany.models.Book;
import com.yourcompany.models.Client;
import com.yourcompany.models.Order;
import com.yourcompany.models.Request;
import org.hibernate.HibernateException;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface IBookStore {
    void addBook(String name, String author, String description, Date published, double price, int countInStock) throws HibernateException;
    void deleteBook(String bookName) throws HibernateException;
    List<Book> getBooks() throws HibernateException;
    List<Book> getStaledBooks() throws HibernateException;
    void addInStock(String bookName, int count) throws HibernateException;
    void debitFromStock(String bookName) throws HibernateException;

    List<Order> getOrders() throws HibernateException;
    Order getOrderById(long id) throws HibernateException;
    Order createOrder(double discount, Date executionDate, String clientName, String... bookNames) throws HibernateException;
    void cancelOrder(long orderId) throws OrderException, HibernateException;
    boolean completeOrder(long orderId) throws OrderException, HibernateException;

    List<Client> getClients() throws HibernateException;
    void addClient(String name, int age) throws HibernateException;

    double getEarnedFundsForTimeSpan(Date from, Date to) throws HibernateException;
    long getCompletedOrdersCountForTimeSpan(Date from, Date to) throws HibernateException;
    List<Order> getCompletedOrdersForTimeSpan(Date from, Date to) throws HibernateException;

    boolean makeRequest(String bookName, int count) throws HibernateException;
    List<Request> getBookRequests(String bookName) throws HibernateException;
    List<Request> getRequests() throws HibernateException;

    void importBooksFromCSVFile(String filename)  throws IOException, HibernateException;
    void importOrdersFromCSVFile(String filename)  throws IOException, HibernateException;
    void importClientsFromCSVFile(String filename)  throws IOException, HibernateException;
    void importRequestsFromCSVFile(String filename)  throws IOException, HibernateException;

    void exportBooksIntoCSVFile(String filename)  throws IOException, HibernateException;
    void exportOrdersIntoCSVFile(String filename)  throws IOException, HibernateException;
    void exportClientsIntoCSVFile(String filename)  throws IOException, HibernateException;
    void exportRequestsIntoCSVFile(String filename)  throws IOException, HibernateException;

//    void getSerializedObjects()  throws IOException;
}
