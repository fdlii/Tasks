package com.task_11_3.interfaces;

import com.task_3_4.Book;
import com.task_3_4.Client;
import com.task_3_4.Order;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface IOrderDAO extends IGenericDAO<Order, Integer> {
    List<Book> getStaledBooks(Timestamp currentTime);
    double getEarnedFundsForTimeSpan(Date from, Date to);
    int getCompletedOrdersCountForTimeSpan(Date from, Date to);
    List<Order> getCompletedOrdersForTimeSpan(Date from, Date to);
    void createOrder(Order order);
    void updateOrder(Order order);
    List<Order> findOrdersByClientId(Client client) throws SQLException;
}
