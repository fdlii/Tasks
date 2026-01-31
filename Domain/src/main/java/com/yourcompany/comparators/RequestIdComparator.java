package com.yourcompany.comparators;

import com.yourcompany.models.Request;

import java.util.Comparator;

public class RequestIdComparator implements Comparator<Request> {
    @Override
    public int compare(Request request1, Request request2) {
        return (int)(request1.getId() - request2.getId());
    }
}
