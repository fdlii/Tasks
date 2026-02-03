package com.yourcompany.DTO;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ClientDTO {
    private final long id;
    private final String name;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    private final int age;
}
