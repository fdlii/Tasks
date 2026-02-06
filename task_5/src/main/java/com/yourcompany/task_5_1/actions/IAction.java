package com.yourcompany.task_5_1.actions;


import com.yourcompany.exceptions.BookException;
import com.yourcompany.exceptions.ClientException;
import com.yourcompany.exceptions.OrderException;

import java.io.IOException;

public interface IAction {
    void execute() throws IOException, BookException, ClientException, OrderException;
}
