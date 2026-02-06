package com.yourcompany.task_3_4.interfaces;

import com.yourcompany.models.Book;
import com.yourcompany.models.Client;
import com.yourcompany.models.Order;
import com.yourcompany.models.Request;

import java.io.*;
import java.util.List;

public interface IFileManager {
    void importBooksFromCSVFile(String filename, List<Book> books) throws IOException;
    void importOrdersFromCSVFile(String filename, List<Order> orders, List<Client> clients, List<Book> books) throws IOException;
    void importClientsFromCSVFile(String filename, List<Client> clients) throws IOException;
    void importRequestsFromCSVFile(String filename, List<Request> requests, List<Book> books) throws IOException;

    void exportBooksIntoCSVFile(String fileName, List<Book> books)  throws IOException;
    void exportOrdersIntoCSVFile(String fileName, List<Order> orders)  throws IOException;
    void exportClientsIntoCSVFile(String fileName, List<Client> clients)  throws IOException;
    void exportRequestsIntoCSVFile(String fileName, List<Request> requests)  throws IOException;

//    <T> void serializeObjects(List<T> objects, String entitiesCategory)  throws IOException;
//    <T> List<T> deserializeObjects(Class<T> type, String fileName)  throws IOException;
}
