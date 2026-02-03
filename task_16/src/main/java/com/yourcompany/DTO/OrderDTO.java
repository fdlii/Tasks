package com.yourcompany.DTO;

import com.yourcompany.models.OrderStatus;
import lombok.AllArgsConstructor;

import java.util.Date;

@AllArgsConstructor
public class OrderDTO {
    private final long id;
    private final ClientDTO client;
    private final double discount;
    private final double finalPrice;
    private final Date executionDate;
    private final OrderStatus orderStatus;

    public long getId() {
        return id;
    }

    public ClientDTO getClient() {
        return client;
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
}
