package com.yourcompany.services;

import com.yourcompany.entities.ClientEntity;
import com.yourcompany.exceptions.ClientException;
import com.yourcompany.mappers.ClientMapper;
import com.yourcompany.models.Client;
import com.yourcompany.repositories.ClientRepository;
import com.yourcompany.utils.FileManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {
    @Mock
    ClientRepository clientRepository;
    @Mock
    ClientMapper clientMapper;
    @Mock
    FileManager fileManager;

    @InjectMocks
    ClientService clientService;

    //Data
    ClientEntity clientEntity;

    @BeforeEach
    public void setUpData() {
        clientEntity = new ClientEntity(1, "Nikita", 25);
    }

    @Test
    public void getClientsSuccessfullyTest() {
        //Given
        List<ClientEntity> clientEntities = new ArrayList<>();
        clientEntities.add(clientEntity);
        List<Client> clientsTest = new ArrayList<>();
        clientsTest.add(new Client(1, "Nikita", 25));

        //When
        when(clientRepository.findAll()).thenReturn(clientEntities);
        when(clientMapper.toModelsList(clientEntities)).thenReturn(List.of(new Client(1, "Nikita", 25)));

        //Then
        List<Client> clients = clientService.getClients();
        assertEquals(clients.size(), clientsTest.size());
        assertEquals(clients.get(0).getName(), clientsTest.get(0).getName());
        verify(clientRepository, times(1)).findAll();
    }

    @Nested
    class AddClientTestClass {
        @Test
        public void addClientSuccessfullyTest() {
            //Given
            Client clientTest = new Client(1, "Nikita", 25);

            //When
            when(clientMapper.toEntity(any(Client.class), eq(true))).thenReturn(clientEntity);
            when(clientRepository.save(clientEntity)).thenReturn(clientEntity);
            when(clientMapper.toModel(clientEntity)).thenReturn(new Client(1, "Nikita", 25));

            //Then
            assertDoesNotThrow(() -> {
                Client client = clientService.addClient(clientEntity.getName(), clientEntity.getAge());
                assertEquals(client.getName(), clientTest.getName());
                assertEquals(client.getAge(), clientTest.getAge());
                verify(clientRepository, times(1)).save(clientEntity);
            });
        }

        @Test
        public void addClientExceptionTest() {
            //Then
            ClientException exception = assertThrows(
                    ClientException.class,
                    () -> {
                        clientService.addClient(null, 25);
                    }
            );

            assertEquals("Некорректные параметры клиента.", exception.getMessage());
        }
    }

    @Nested
    class importClientsTestClass {
        @Test
        public void importClientsSuccessfullyTest() throws IOException {
            //Given
            String filename = "task_6/src/main/java/com/yourcompany/task_6_1/Clients.csv";
            List<Client> clients = new ArrayList<>();

            //When
            doNothing()
                    .when(fileManager)
                    .importClientsFromCSVFile(filename, clients);
            when(clientMapper.toEntity(any(Client.class), eq(true)))
                    .thenReturn(clientEntity);
            when(clientRepository.save(clientEntity))
                    .thenReturn(clientEntity);

            //Then
            assertDoesNotThrow(() -> {
                clientService.importClientsFromCSVFile(filename);
            });
        }

        @Test
        public void importClientsExceptionTest() throws IOException {
            //Given
            String filename = "";
            List<Client> clients = List.of();

            //When
            doThrow(new IOException("Не удалось прочитать файл."))
                    .when(fileManager)
                    .importClientsFromCSVFile(filename, clients);

            //Then
            IOException exception = assertThrows(IOException.class, () -> {
                clientService.importClientsFromCSVFile(filename);
            });
            assertEquals("Не удалось прочитать файл.", exception.getMessage());
        }
    }

    @Nested
    class exportClientsTestClass {
        @Test
        public void exportClientsSuccessfullyTest() throws IOException {
            //Given
            String filename = "task_6/src/main/java/com/yourcompany/task_6_1/Clients.csv";
            List<Client> clients = new ArrayList<>();

            clients.add(new Client(1, "Nikita", 25));
            //When
            doNothing()
                    .when(fileManager)
                    .exportClientsIntoCSVFile(filename, clients);
            when(clientMapper.toModelsList(any(List.class)))
                    .thenReturn(clients);
            when(clientRepository.findAll())
                    .thenReturn(new ArrayList<ClientEntity>());

            //Then
            assertDoesNotThrow(() -> {
                clientService.exportClientsIntoCSVFile(filename);
            });
            verify(clientRepository, times(1)).findAll();
        }

        @Test
        public void exportClientsExceptionTest() throws IOException {
            //Given
            String filename = "";
            List<Client> clients = new ArrayList<>();

            //When
            doThrow(new IOException("Не удалось прочитать файл."))
                    .when(fileManager)
                    .exportClientsIntoCSVFile(filename, clients);

            //Then
            IOException exception = assertThrows(IOException.class, () -> {
                clientService.exportClientsIntoCSVFile(filename);
            });
            assertEquals("Не удалось прочитать файл.", exception.getMessage());
        }
    }
}
