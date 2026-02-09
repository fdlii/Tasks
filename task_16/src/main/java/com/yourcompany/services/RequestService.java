package com.yourcompany.services;

import com.yourcompany.exceptions.BookNotFoundException;
import com.yourcompany.exceptions.RequestNotFoundException;
import com.yourcompany.mappers.BookMapper;
import com.yourcompany.mappers.RequestMapper;
import com.yourcompany.models.Book;
import com.yourcompany.models.Request;
import com.yourcompany.repositories.BookRepository;
import com.yourcompany.repositories.RequestRepository;
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
    public void makeRequest(String bookName, int count) throws HibernateException, BookNotFoundException {
        logger.info("Создание запроса.");
        try {
            List<Book> books = bookMapper.toModelsList(bookRepository.findAll());

            boolean flag = false;
            for (Book book : books) {
                if (book.getName().equals(bookName)) {
                    flag = true;
                    Request request = new Request(book, count);
                    requestRepository.save(requestMapper.toEntity(request, true));
                    logger.info("Запрос успешно создан.");
                }
            }
            if (!flag) {
                logger.error("Не удалось найти запрашиваемую книгу.");
                throw new BookNotFoundException("Не удалось найти запрашиваемую книгу.");
            }
        } catch (HibernateException ex) {
            logger.error("Не удалось добавить запрос в БД.");
            throw new HibernateException(ex);
        }
    }

    @Transactional
    public List<Request> getBookRequests(String bookName) throws HibernateException, BookNotFoundException, RequestNotFoundException {
        logger.info("Получение запросов у книги.");
        try {
            Book book = bookMapper.toModel(bookRepository.findByName(bookName));
            List<Request> requests = requestMapper.toModelsList(requestRepository.findByBook(bookMapper.toEntity(book, false)));
            logger.info("Запросы успешно получены.");
            return requests;
        } catch (HibernateException ex) {
            logger.error("Не удалось получить запросы книги.");
            throw new HibernateException(ex);
        }
        catch (BookNotFoundException ex) {
            logger.error("Не удалось найти запрашиваемую книгу.");
            throw new BookNotFoundException("Не удалось найти запрашиваемую книгу.");
        }
    }

    @Transactional
    public List<Request> getRequests() throws HibernateException, RequestNotFoundException {
        logger.info("Получение запросов.");
        List<Request> requests;
        try {
            requests = requestMapper.toModelsList(requestRepository.findAll());
            logger.info("Запросы успешно получены");
        } catch (HibernateException ex) {
            logger.error("Не удалось получить запросы из БД.");
            throw new HibernateException(ex);
        }
        return requests;
    }

    @Transactional
    public void importRequestsFromCSVFile(String filename) throws IOException, HibernateException, BookNotFoundException {
        logger.info("Импорт запросов.");
        try {
            List<Book> books = bookMapper.toModelsList(bookRepository.findAll());
            List<Request> requests = new ArrayList<>();
            fileManager.importRequestsFromCSVFile(filename, requests, books);
            for (Request request : requests) {
                requestRepository.save(requestMapper.toEntity(request, true));
            }
            logger.info("Запросы успешно импортированы.");
        } catch (HibernateException ex) {
            logger.error("Не удалось импортировать запросы.");
            throw new HibernateException(ex);
        }
    }

    @Transactional
    public void exportRequestsIntoCSVFile(String filename) throws IOException, HibernateException, RequestNotFoundException {
        logger.info("Экспорт запросов.");
        try {
            fileManager.exportRequestsIntoCSVFile(filename, requestMapper.toModelsList(requestRepository.findAll()));
            logger.info("Запросы успешно экспортированы.");
        } catch (HibernateException ex) {
            logger.error("Не удалось экспортировать запросы.");
            throw new HibernateException(ex);
        }
    }
}
