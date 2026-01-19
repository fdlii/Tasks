package com.task_11_3.interfaces;

import com.task_3_4.Book;
import com.task_3_4.Request;

import java.sql.SQLException;
import java.util.List;

public interface IBookDAO extends IGenericDAO<Book, Integer> {
    void createBook(Book book) throws SQLException;
    void deleteBook(String bookName) throws SQLException;
    boolean addBookInStock(String bookName, int count) throws SQLException;
    boolean debitBookFromStock(String bookName) throws SQLException;
    List<Request> getBookRequests(String bookName) throws SQLException;
}
