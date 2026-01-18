package com.task_13.DAOs;

import com.task_13.entities.ClientEntity;

import java.util.List;

public class ClientDAO implements DAO<ClientEntity, Long> {
    @Override
    public ClientEntity findById(Long aLong) {
        return null;
    }

    @Override
    public List<ClientEntity> findAll() {
        return List.of();
    }

    @Override
    public ClientEntity save(ClientEntity entity) {
        return null;
    }

    @Override
    public ClientEntity update(ClientEntity entity) {
        return null;
    }

    @Override
    public void delete(ClientEntity entity) {

    }
}
