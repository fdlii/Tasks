package com.task_4_1;

import com.task_3_4.Request;

import java.util.Comparator;

public class RequestIdComparator implements Comparator<Request> {
    @Override
    public int compare(Request request1, Request request2) {
        return request1.getId() - request2.getId();
    }
}
