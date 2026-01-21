package com.task_13.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "clients")
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private int age;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<OrderEntity> orders;

    public ClientEntity(long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public ClientEntity(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
