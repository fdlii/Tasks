package com.task_11_3.interfaces;

import com.task_3_4.Book;
import com.task_3_4.Request;

import java.util.List;

public interface IBookDAO extends IGenericDAO<Book, Integer> {
    void createBook(Book book);
    void deleteBook(String bookName);
    boolean addBookInStock(String bookName, int count);
    boolean debitBookFromStock(String bookName);
    List<Request> getBookRequests(String bookName);
}
