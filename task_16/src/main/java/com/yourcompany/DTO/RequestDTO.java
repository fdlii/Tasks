package com.yourcompany.DTO;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RequestDTO {
    private final long id;
    private final BookDTO book;
    private final int count;
    private final boolean isOpen;

    public long getId() {
        return id;
    }

    public BookDTO getBook() {
        return book;
    }

    public int getCount() {
        return count;
    }

    public boolean isOpen() {
        return isOpen;
    }
}
