package com.yourcompany.services;

import com.yourcompany.exceptions.ClientException;
import com.yourcompany.mappers.ClientMapper;
import com.yourcompany.models.Client;
import com.yourcompany.repositories.ClientRepository;
import com.yourcompany.utils.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClientService {
    Logger logger = LoggerFactory.getLogger(ClientService.class);
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ClientMapper clientMapper;
    @Autowired
    FileManager fileManager;

    @Transactional
    public List<Client> getClients() {
        logger.info("Получение всех клиентов.");
        List<Client> clients = clientMapper.toModelsList(clientRepository.findAll());
        logger.info("Клиенты успешно получены.");
        return clients;
    }

    @Transactional
    public Client addClient(String name, int age) throws ClientException {
        logger.info("Добавление клиента.");
        if (name == null || name.isEmpty() || age < 1 || age > 110) {
            logger.error("Некорректные параметры клиента.");
            throw new ClientException("Некорректные параметры клиента.");
        }
        Client client = clientMapper.toModel(clientRepository.save(clientMapper.toEntity(new Client(name, age), true)));
        logger.info("Клиент успешно добавлен.");
        return client;
    }

    @Transactional
    public void importClientsFromCSVFile(String filename) throws IOException {
        logger.info("Импорт клиентов.");
        List<Client> clients = new ArrayList<>();
        fileManager.importClientsFromCSVFile(filename, clients);
        for (Client client : clients) {
            clientRepository.save(clientMapper.toEntity(client, true));
        }
        logger.info("Клиенты успешно импортированы.");

    }

    @Transactional
    public void exportClientsIntoCSVFile(String filename) throws IOException {
        logger.info("Экспорт клиентов.");
        fileManager.exportClientsIntoCSVFile(filename, clientMapper.toModelsList(clientRepository.findAll()));
        logger.info("Клиенты успешно экспортированы.");
    }
}
