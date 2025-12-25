package com.task_11_3;

import com.task_11_3.implementations.RequestDAOImplementation;
import com.task_3_4.Book;
import com.task_3_4.Request;

public class TestClass {
    public static void main(String[] args) {
        RequestDAOImplementation requestDAOImplementation = new RequestDAOImplementation("requests");
        Request request = new Request(new Book(), 5);
        requestDAOImplementation.createRequest(request);
    }
}
