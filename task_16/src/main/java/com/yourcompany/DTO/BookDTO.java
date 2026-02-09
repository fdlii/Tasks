package com.yourcompany.DTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private long id;
    private String name;
    private String author;
    private String description;
    private Date published;
    private boolean inStock;
    private int countInStock;
    private double price;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public Date getPublished() {
        return published;
    }

    public boolean isInStock() {
        return inStock;
    }

    public int getCountInStock() {
        return countInStock;
    }

    public double getPrice() {
        return price;
    }
}
