package com.yourcompany.services;

import com.yourcompany.mappers.ClientMapper;
import com.yourcompany.models.Client;
import com.yourcompany.repositories.ClientRepository;
import org.hibernate.HibernateException;
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
    public List<Client> getClients() throws HibernateException {
        logger.info("Получение всех клиентов.");
        List<Client> clients = null;
        try {
            clients = clientMapper.toModelsList(clientRepository.findAll());
            logger.info("Клиенты успешно получены.");
        }
        catch (HibernateException ex) {
            logger.error("Ошибка при получении клиентов из БД.");
            throw new HibernateException(ex);
        }
        return clients;
    }

    @Transactional
    public void addClient(String name, int age) throws HibernateException {
        logger.info("Добавление клиента.");
        try {
            clientRepository.save(clientMapper.toEntity(new Client(name, age), true));
            logger.info("Клиент успешно добавлен.");
        } catch (HibernateException ex) {
            logger.error("Не удалось добавить клиента в БД.");
            throw new HibernateException(ex);
        }
    }

    @Transactional
    public void importClientsFromCSVFile(String filename) throws IOException, HibernateException {
        logger.info("Импорт клиентов.");
        List<Client> clients = new ArrayList<>();
        fileManager.importClientsFromCSVFile(filename, clients);
        try {
            for (Client client : clients) {
                clientRepository.save(clientMapper.toEntity(client, true));
            }
            logger.info("Клиенты успешно импортированы.");
        } catch (HibernateException ex) {
            logger.error("Не удалось импортировать клиентов.");
            throw new HibernateException(ex);
        }
    }

    @Transactional
    public void exportClientsIntoCSVFile(String filename) throws IOException, HibernateException {
        logger.info("Экспорт клиентов.");
        try {
            fileManager.exportClientsIntoCSVFile(filename, clientMapper.toModelsList(clientRepository.findAll()));
            logger.info("Клиенты успешно экспортированы.");
        } catch (HibernateException ex) {
            logger.error("Не удалось экспортировать клиентов.");
            throw new HibernateException(ex);
        }
    }
}
