package com.yourcompany.task_3_4.mappers;

import com.yourcompany.models.Client;
import com.yourcompany.task_13.entities.ClientEntity;

import java.util.ArrayList;
import java.util.List;

public class ClientMapper implements IMapper<Client, ClientEntity> {

    @Override
    public Client toModel(ClientEntity entity) {
        return new Client(
                entity.getId(),
                entity.getName(),
                entity.getAge()
        );
    }

    @Override
    public ClientEntity toEntity(Client model, boolean ignoreId) {
        if (ignoreId) {
            return new ClientEntity(
                 model.getName(),
                 model.getAge()
            );
        }
        return new ClientEntity(
                model.getId(),
                model.getName(),
                model.getAge()
        );
    }

    @Override
    public List<Client> toModelsList(List<ClientEntity> entities) {
        List<Client> clients = new ArrayList<>();
        for (ClientEntity clientEntity : entities) {
            clients.add(toModel(clientEntity));
        }
        return clients;
    }
}
