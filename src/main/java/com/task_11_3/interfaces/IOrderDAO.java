package com.task_11_3.interfaces;

import com.task_3_4.Book;
import com.task_3_4.Client;
import com.task_3_4.Order;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface IOrderDAO extends IGenericDAO<Order, Integer> {
    List<Book> getStaledBooks(Timestamp currentTime) throws SQLException;
    double getEarnedFundsForTimeSpan(Date from, Date to) throws SQLException;
    int getCompletedOrdersCountForTimeSpan(Date from, Date to) throws SQLException;
    List<Order> getCompletedOrdersForTimeSpan(Date from, Date to) throws SQLException;
    void createOrder(Order order) throws SQLException;
    void updateOrder(Order order) throws SQLException;
    List<Order> findOrdersByClientId(Client client) throws SQLException;
}
