package com.yourcompany.DTO;
import lombok.AllArgsConstructor;

import java.util.Date;

@AllArgsConstructor
public class BookDTO {
    private final long id;
    private final String name;
    private final String author;
    private final String description;
    private final Date published;
    private final boolean inStock;
    private final int countInStock;
    private final double price;

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
