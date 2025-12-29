package com.task_11_3.interfaces;

import com.task_3_4.Book;
import com.task_3_4.Request;

import java.sql.SQLException;
import java.util.List;

public interface IRequestDAO extends IGenericDAO<Request, Integer> {
    void createRequest(Request request);
    void updateRequest(Request request);
    public List<Request> findRequestsByBookId(Book book) throws SQLException;
}
