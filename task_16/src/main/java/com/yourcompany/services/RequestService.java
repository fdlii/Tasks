package com.yourcompany.services;

import com.yourcompany.exceptions.BookNotFoundException;
import com.yourcompany.exceptions.RequestException;
import com.yourcompany.mappers.BookMapper;
import com.yourcompany.mappers.RequestMapper;
import com.yourcompany.models.Book;
import com.yourcompany.models.Request;
import com.yourcompany.repositories.BookRepository;
import com.yourcompany.repositories.RequestRepository;
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
public class RequestService {
    Logger logger = LoggerFactory.getLogger(RequestService.class);
    @Autowired
    RequestRepository requestRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    RequestMapper requestMapper;
    @Autowired
    BookMapper bookMapper;
    @Autowired
    FileManager fileManager;

    @Transactional
    public Request makeRequest(String bookName, int count) throws BookNotFoundException, RequestException {
        logger.info("Создание запроса.");
        if (bookName == null || count < 1) {
            throw new RequestException("Некорректные параметры запроса.");
        }
        try {
            Book book = bookMapper.toModel(bookRepository.findByName(bookName));
            Request request = new Request(book, count);
            requestRepository.save(requestMapper.toEntity(request, true));
            logger.info("Запрос успешно создан.");
            return request;
        } catch (NullPointerException ex) {
            logger.error(ex.getMessage());
            throw new BookNotFoundException(ex.getMessage());
        }
    }

    @Transactional
    public List<Request> getBookRequests(String bookName) throws BookNotFoundException {
        logger.info("Получение запросов у книги.");
        try {
            Book book = bookMapper.toModel(bookRepository.findByName(bookName));
            List<Request> requests = requestMapper.toModelsList(requestRepository.findByBook(bookMapper.toEntity(book, false)));
            logger.info("Запросы успешно получены.");
            return requests;
        }
        catch (NullPointerException ex) {
            logger.error(ex.getMessage());
            throw new BookNotFoundException(ex.getMessage());
        }
    }

    @Transactional
    public List<Request> getRequests() {
        logger.info("Получение запросов.");
        List<Request> requests = requestMapper.toModelsList(requestRepository.findAll());
        logger.info("Запросы успешно получены");
        return requests;
    }

    @Transactional
    public void importRequestsFromCSVFile(String filename) throws IOException {
        logger.info("Импорт запросов.");
        List<Book> books = bookMapper.toModelsList(bookRepository.findAll());
        List<Request> requests = new ArrayList<>();
        fileManager.importRequestsFromCSVFile(filename, requests, books);
        for (Request request : requests) {
            requestRepository.save(requestMapper.toEntity(request, true));
        }
        logger.info("Запросы успешно импортированы.");
    }

    @Transactional
    public void exportRequestsIntoCSVFile(String filename) throws IOException {
        logger.info("Экспорт запросов.");
        fileManager.exportRequestsIntoCSVFile(filename, requestMapper.toModelsList(requestRepository.findAll()));
        logger.info("Запросы успешно экспортированы.");
    }
}
