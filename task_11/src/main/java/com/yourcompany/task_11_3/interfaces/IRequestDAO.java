package com.yourcompany.task_11_3.interfaces;

import com.yourcompany.models.Book;
import com.yourcompany.models.Request;

import java.sql.SQLException;
import java.util.List;

public interface IRequestDAO extends IGenericDAO<Request, Integer> {
    void createRequest(Request request) throws SQLException;
    void updateRequest(Request request) throws SQLException;
    public List<Request> findRequestsByBookId(Book book) throws SQLException;
}
