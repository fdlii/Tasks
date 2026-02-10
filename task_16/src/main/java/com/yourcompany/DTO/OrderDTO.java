package com.yourcompany.DTO;

import com.yourcompany.models.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private long id;
    private String clientName;
    private double discount;
    private double finalPrice;
    private Date executionDate;
    private OrderStatus orderStatus;
    public List<String> bookNames;

    public long getId() {
        return id;
    }

    public String getClientName() {
        return clientName;
    }

    public double getDiscount() {
        return discount;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public Date getExecutionDate() {
        return executionDate;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<String> getBookNames() {
        return bookNames;
    }
}
