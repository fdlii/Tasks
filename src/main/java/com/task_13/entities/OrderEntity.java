package com.task_13.entities;

import com.task_3_4.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clientid")
    private ClientEntity client;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "orders_books",
            joinColumns = @JoinColumn(name = "orderid"),
            inverseJoinColumns = @JoinColumn(name = "bookid")
    )
    private List<BookEntity> books;

    private double discount;

    @Column(name = "finalprice")
    private double finalPrice;

    @Column(name = "executiondate")
    private LocalDateTime executionDate;

    @Column(name = "orderstatus")
    @Enumerated
    private OrderStatus orderStatus;

    public OrderEntity(long id, ClientEntity clientEntity, double discount, double finalPrice, LocalDateTime executionDate, OrderStatus orderStatus) {
        this.id = id;
        this.client = clientEntity;
        this.discount = discount;
        this.finalPrice = finalPrice;
        this.executionDate = executionDate;
        this.orderStatus = orderStatus;
    }

    public OrderEntity(ClientEntity clientEntity, double discount, double finalPrice, LocalDateTime executionDate, OrderStatus orderStatus) {
        this.client = clientEntity;
        this.discount = discount;
        this.finalPrice = finalPrice;
        this.executionDate = executionDate;
        this.orderStatus = orderStatus;
    }
}
