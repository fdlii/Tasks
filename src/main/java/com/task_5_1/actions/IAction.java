package com.task_5_1.actions;

import com.task_6_2.BookExeption;
import com.task_6_2.ClientExeption;
import com.task_6_2.OrderExeption;

import java.io.IOException;

public interface IAction {
    void execute() throws IOException, BookExeption, ClientExeption, OrderExeption;
}
