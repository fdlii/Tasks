package com.yourcompany;

import com.yourcompany.entities.ClientEntity;
import com.yourcompany.exceptions.ClientNotFoundException;
import com.yourcompany.mappers.ClientMapper;
import com.yourcompany.models.Client;
import com.yourcompany.repositories.ClientRepository;
import com.yourcompany.services.ClientService;
import com.yourcompany.utils.FileManager;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {
    @Mock
    ClientRepository clientRepository;
    @Mock
    ClientMapper clientMapper;
    @Mock
    FileManager fileManager;

    //Data
    ClientEntity clientEntity;

    @InjectMocks
    ClientService clientService;

    @BeforeEach
    public void setUpData() {
        clientEntity = new ClientEntity(1, "Nikita", 25);
    }

    @Test
    @DisplayName("Get clients successfully test")
    @SneakyThrows
    public void getClientsTest() {
        //Given
        List<ClientEntity> clientEntities = new ArrayList<>();
        clientEntities.add(clientEntity);

        List<Client> clients = new ArrayList<>();
        clients.add(new Client(1, "Nikita", 25));
        //When
        when(clientRepository.findAll()).thenReturn(clientEntities);
        when(clientMapper.toModelsList(clientEntities)).thenReturn(List.of(new Client(1, "Nikita", 25)));
        //Then
        assertEquals(clientService.getClients().size(), clients.size());
        assertEquals(clientService.getClients().get(0).getName(), clients.get(0).getName());
    }
}
