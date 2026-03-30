package com.yourcompany.DTO;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class RequestDTO {
    private long id;
    private String bookName;
    private int count;
    private boolean isOpen;

    public long getId() {
        return id;
    }

    public String getBookName() {
        return bookName;
    }

    public int getCount() {
        return count;
    }

    public boolean isOpen() {
        return isOpen;
    }
}
