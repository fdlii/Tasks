package com.yourcompany.task_13.DAOs;

import com.yourcompany.task_13.entities.ClientEntity;
import org.springframework.stereotype.Repository;


@Repository
public class ClientDAO extends GenericDAO<ClientEntity, Long> {

    public ClientDAO() {
        super(ClientEntity.class);
    }
}
