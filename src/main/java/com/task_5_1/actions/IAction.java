package com.task_5_1.actions;

import com.task_6_2.BookException;
import com.task_6_2.ClientException;
import com.task_6_2.OrderException;

import java.io.IOException;

public interface IAction {
    void execute() throws IOException, BookException, ClientException, OrderException;
}
