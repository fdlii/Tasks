package com.yourcompany.services;

import com.yourcompany.mappers.BookMapper;
import com.yourcompany.mappers.RequestMapper;
import com.yourcompany.models.Book;
import com.yourcompany.models.Request;
import com.yourcompany.task_13.DAOs.BookDAO;
import com.yourcompany.task_13.DAOs.RequestDAO;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RequestService {
    Logger logger = LoggerFactory.getLogger(RequestService.class);
    @Autowired
    RequestDAO requestDAO;
    @Autowired
    BookDAO bookDAO;
    @Autowired
    RequestMapper requestMapper;
    @Autowired
    BookMapper bookMapper;
    @Autowired
    FileManager fileManager;

    @Transactional
    public boolean makeRequest(String bookName, int count) throws HibernateException {
        logger.info("Создание запроса.");
        try {
            List<Book> books = bookMapper.toModelsList(bookDAO.findAll());

            for (Book book : books) {
                if (book.getName().equals(bookName)) {
                    Request request = new Request(book, count);
                    requestDAO.save(requestMapper.toEntity(request, true));
                    logger.info("Запрос успешно создан.");
                    return true;
                }
            }
        } catch (HibernateException ex) {
            logger.error("Не удалось добавить запрос в БД.");
            throw new HibernateException(ex);
        }
        return false;
    }

    @Transactional
    public List<Request> getBookRequests(String bookName) throws HibernateException {
        logger.info("Получение запросов у книги.");
        try {
            Book book = bookMapper.toModel(bookDAO.findByName(bookName));
            List<Request> requests = requestMapper.toModelsList(requestDAO.findRequestsByBookId(book.getId()));
            logger.info("Запросы успешно получены.");
            return requests;
        } catch (HibernateException ex) {
            logger.error("Не удалось получить запросы книги.");
            throw new HibernateException(ex);
        }
    }

    @Transactional
    public List<Request> getRequests() throws HibernateException {
        logger.info("Получение запросов.");
        List<Request> requests;
        try {
            requests = requestMapper.toModelsList(requestDAO.findAll());
            logger.info("Запросы успешно получены");
        } catch (HibernateException ex) {
            logger.error("Не удалось получить запросы из БД.");
            throw new HibernateException(ex);
        }
        return requests;
    }

    @Transactional
    public void importRequestsFromCSVFile(String filename) throws IOException, HibernateException {
        logger.info("Импорт запросов.");
        try {
            List<Book> books = bookMapper.toModelsList(bookDAO.findAll());
            List<Request> requests = new ArrayList<>();
            fileManager.importRequestsFromCSVFile(filename, requests, books);
            for (Request request : requests) {
                requestDAO.save(requestMapper.toEntity(request, true));
            }
            logger.info("Запросы успешно импортированы.");
        } catch (HibernateException ex) {
            logger.error("Не удалось импортировать запросы.");
            throw new HibernateException(ex);
        }
    }

    @Transactional
    public void exportRequestsIntoCSVFile(String filename) throws IOException, HibernateException {
        logger.info("Экспорт запросов.");
        try {
            fileManager.exportRequestsIntoCSVFile(filename, requestMapper.toModelsList(requestDAO.findAll()));
            logger.info("Запросы успешно экспортированы.");
        } catch (HibernateException ex) {
            logger.error("Не удалось экспортировать запросы.");
            throw new HibernateException(ex);
        }
    }
}
