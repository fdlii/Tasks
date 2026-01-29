package com.yourcompany.task_13.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "books")
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String author;

    private String description;

    private LocalDateTime published;

    @Column(name = "instock")
    private boolean inStock;

    @Column(name = "countinstock")
    private int countInStock;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<RequestEntity> requests;

    private double price;

    public BookEntity(long id, String name, String author, String description, LocalDateTime published, boolean inStock, int countInStock, double price) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.description = description;
        this.published = published;
        this.inStock = inStock;
        this.countInStock = countInStock;
        this.price = price;
    }

    public BookEntity(String name, String author, String description, LocalDateTime published, boolean inStock, int countInStock, double price) {
        this.name = name;
        this.author = author;
        this.description = description;
        this.published = published;
        this.inStock = inStock;
        this.countInStock = countInStock;
        this.price = price;
    }
}
