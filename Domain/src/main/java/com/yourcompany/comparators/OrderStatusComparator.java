package com.yourcompany.comparators;

import com.yourcompany.models.Order;

import java.util.Comparator;

public class OrderStatusComparator implements Comparator<Order> {
    @Override
    public int compare(Order order1, Order order2) {
        return order1.getOrderStatus().compareTo(order2.getOrderStatus());
    }
}
