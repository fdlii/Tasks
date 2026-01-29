package com.yourcompany.task_11_3.interfaces;

import com.yourcompany.models.Client;

import java.sql.SQLException;

public interface IClientDAO extends IGenericDAO<Client, Integer> {
    void createClient(Client client) throws SQLException;
}
