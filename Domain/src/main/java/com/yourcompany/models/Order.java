package com.yourcompany.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yourcompany.exceptions.OrderException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonAutoDetect
public class Order {
    private static int counter = 1;

    private long id;
    @JsonIgnore
    private Client client;
    private List<Book> books;
    private double discount;
    private double finalPrice;
    private Date executionDate;
    private OrderStatus orderStatus;

    public Order(){}

    public Order(double discount, Date executionDate, Client client) {
        this.id = counter++; //заглушка
        this.discount = discount;
        books = new ArrayList<>();
        this.client = client;
        this.executionDate = executionDate;
        this.orderStatus = OrderStatus.NEW;
    }

    public Order(Client client, double discount, double finalPrice, Date executionDate, OrderStatus orderStatus) {
        this.discount = discount;
        this.finalPrice = finalPrice;
        books = new ArrayList<>();
        this.client = client;
        this.executionDate = executionDate;
        this.orderStatus = orderStatus;
    }

    public Order(long id, Client client, double discount, double finalPrice, Date executionDate, OrderStatus orderStatus) {
        this.id = id;
        if (id == counter) {
            counter++;
        }
        this.discount = discount;
        this.finalPrice = finalPrice;
        books = new ArrayList<>();
        this.client = client;
        this.executionDate = executionDate;
        this.orderStatus = orderStatus;
    }

    public long getId() {
        return id;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public Date getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
        calculateFinalPrice();
    }

    public List<Book> getBooks() {
        return books;
    }

    public void addBook(Book book) {
        books.add(book);
        calculateFinalPrice();
    }

    public void deleteBook(Book book) {
        books.remove(book);
        calculateFinalPrice();
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void changeStatus(OrderStatus orderStatus) throws OrderException {
        if (orderStatus == OrderStatus.COMPLETED) {
            for (Book book : books) {
                for (Request request : book.getRequests()) {
                    if (request.isOpen()){
                        throw new OrderException("Невозможно завершить заказ. Книга отсвутствует на складе.");
                    }
                }
            }
            this.orderStatus = orderStatus;
        }
        if (orderStatus == OrderStatus.CANCELLED) {
            for (Book book : books) {
                for (Request request : book.getRequests()){
                    if (request.getCount() == 1) {
                        request.setOpen(false);
                        break;
                    }
                }
            }
            this.orderStatus = orderStatus;
        }
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    private void calculateFinalPrice() {
        this.finalPrice = 0;
        for (Book book : books) {
            this.finalPrice += book.getPrice() * (1 - discount);
        }
    }
}
