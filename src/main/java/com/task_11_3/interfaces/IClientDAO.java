package com.task_11_3.interfaces;

import com.task_3_4.Book;
import com.task_3_4.Client;
import com.task_3_4.Order;

public interface IClientDAO extends IGenericDAO<Client, Integer> {
    void createClient(Client client);
}
