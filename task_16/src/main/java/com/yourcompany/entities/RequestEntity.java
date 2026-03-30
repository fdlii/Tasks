package com.yourcompany.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "requests")
public class RequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookid")
    private BookEntity book;

    private int count;

    @Column(name = "isopen")
    private boolean isOpen;

    public RequestEntity(BookEntity bookEntity, int count, boolean open) {
        this.book = bookEntity;
        this.count = count;
        this.isOpen = open;
    }
}
