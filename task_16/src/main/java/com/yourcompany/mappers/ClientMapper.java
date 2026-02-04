package com.yourcompany.mappers;

import com.yourcompany.DTO.ClientDTO;
import com.yourcompany.entities.ClientEntity;
import com.yourcompany.models.Client;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientMapper implements IMapper<ClientDTO, Client, ClientEntity> {

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

    @Override
    public Client fromDTOtoModel(ClientDTO DTO) {
        return new Client(
            DTO.getId(),
            DTO.getName(),
            DTO.getAge()
        );
    }

    @Override
    public ClientDTO fromModelToDTO(Client model) {
        return new ClientDTO(
                model.getId(),
                model.getName(),
                model.getAge()
        );
    }

    @Override
    public List<ClientDTO> toDTOList(List<Client> models) {
        List<ClientDTO> clientDTOList = new ArrayList<>();
        for (Client client : models) {
            clientDTOList.add(fromModelToDTO(client));
        }
        return clientDTOList;
    }
}
