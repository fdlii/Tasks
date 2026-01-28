package com.task_3_4;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonAutoDetect
public class Book {
    private static int counter = 1;

    private long id;
    private String name;
    private String author;
    private String description;
    private Date published;
    private boolean inStock;
    private int countInStock;
    private List<Request> requests = new ArrayList<Request>();
    private double price;

    public Book() {
        this.id = counter++;
    }

    public Book(String name, String author, String description, Date published, double price, int countInStock) {
        this.name = name;
        this.author = author;
        this.description = description;
        this.published = published;
        this.price = price;
        this.countInStock = countInStock;
        if (countInStock > 0) {
            this.inStock = true;
        }
        else {
            this.inStock = false;
        }
    }

    public Book(long id, String name, String author, String description, Date published, int countInStock, double price) {
        this.id = id;
        if (id == counter) {
            counter++;
        }
        this.name = name;
        this.author = author;
        this.description = description;
        this.published = published;
        this.price = price;
        this.countInStock = countInStock;
        if (countInStock > 0) {
            this.inStock = true;
        }
        else {
            this.inStock = false;
        }
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public int getCountInStock() {
        return countInStock;
    }

    public void setCountInStock(int countInStock) {
        this.countInStock = countInStock;
        if (countInStock > 0) {
            setInStock(true);
        }
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void addRequest(Request request) {
        requests.add(request);
    }

    public void deleteRequest(Request request) {
        requests.remove(request);
    }

    public void changeRequest(int requestId, boolean status) {
        requests.stream()
                .filter(request -> request.getId() == requestId)
                .forEach(request -> request.setOpen(status));
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    public List<Request> getRequests()
    {
        return requests;
    }
}
