package com.yourcompany.services;

import com.yourcompany.comparators.BookIsInStockComparator;
import com.yourcompany.comparators.BookNameComparator;
import com.yourcompany.comparators.BookPriceComparator;
import com.yourcompany.comparators.BookPublishedComparator;
import com.yourcompany.mappers.BookMapper;
import com.yourcompany.models.Book;
import com.yourcompany.repositories.BookRepository;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BookService {
    Logger logger = LoggerFactory.getLogger(BookService.class);
    @Autowired
    BookRepository bookRepository;
    @Autowired
    BookMapper bookMapper;
    @Autowired
    FileManager fileManager;

    @Transactional
    public void addBook(String name, String author, String description, Date published, double price, int countInStock) throws HibernateException {
        logger.info("Добавление книги.");
        try {
            bookRepository.save(bookMapper.toEntity(new Book(name, author, description, published, price, countInStock), true));
            logger.info("Книга успешно добавлена.");
        } catch (HibernateException ex) {
            logger.error("Не удалось добавить книгу в БД.");
            throw new HibernateException(ex);
        }
    }

    @Transactional
    public void deleteBook(String bookName) throws HibernateException {
        logger.info("Удаление книги.");
        try {
            bookRepository.delete(bookRepository.findByName(bookName));
            logger.info("Книга успешно удалена.");
        } catch (HibernateException ex) {
            logger.error("Не удалось удалить книгу из БД.");
            throw new HibernateException(ex);
        }
    }

    @Transactional
    public List<Book> getBooks() throws HibernateException {
        logger.info("Получение всех книг.");
        List<Book> books;
        try {
            books = bookMapper.toModelsList(bookRepository.findAll());
            logger.info("Книги успешно получены.");
        } catch (HibernateException ex) {
            logger.error("Ошибка при получении книг из БД.");
            throw new HibernateException(ex);
        }
        books.sort(new BookNameComparator()
                .thenComparing(new BookPublishedComparator()
                        .thenComparing(new BookPriceComparator()
                                .thenComparing(new BookIsInStockComparator()))));
        return books;
    }

    @Transactional
    public List<Book> getStaledBooks() throws HibernateException {
        logger.info("Получение залежавшихся книг.");
        List<Book> staledBooks;

        LocalDateTime threshold = LocalDateTime.now().minusMonths(6);

        try {
            staledBooks = bookMapper.toModelsList(bookRepository.getStaledBooks(threshold));
            logger.info("Книги успешно получены.");
        } catch (HibernateException ex) {
            logger.error("Ошибка при получении книг из БД.");
            throw new HibernateException(ex);
        }
        staledBooks.sort(new BookPublishedComparator()
                .thenComparing(new BookPriceComparator()));
        return staledBooks;
    }

    @Transactional
    public void addInStock(String bookName, int count) throws HibernateException {
        logger.info("Добавление книги на склад.");
        try {
            Book book = bookMapper.toModel(bookRepository.findByName(bookName));
            book.setCountInStock(book.getCountInStock() + count);
            bookRepository.save(bookMapper.toEntity(book, false));
            logger.info("Книга успешно добавлена на склад.");
        } catch (HibernateException ex) {
            logger.error("Не удалось добавить книгу на склад.");
            System.out.println(ex.getMessage());
            throw new HibernateException(ex);
        }
    }

    @Transactional
    public void debitFromStock(String bookName) throws HibernateException {
        logger.info("Удаление книги со склада.");
        try {
            Book book = bookMapper.toModel(bookRepository.findByName(bookName));
            book.setCountInStock(0);
            book.setInStock(false);
            bookRepository.save(bookMapper.toEntity(book, false));
            logger.info("Книга успешно удалена со склада.");
        } catch (HibernateException ex) {
            System.out.println(ex.getMessage());
            logger.error("Не удалось удалить книгу со склада.");
            throw new HibernateException(ex);
        }
    }

    @Transactional
    public void importBooksFromCSVFile(String filename) throws IOException, HibernateException {
        logger.info("Импорт книг.");
        try {
            List<Book> books = new ArrayList<>();
            fileManager.importBooksFromCSVFile(filename, books);
            for (Book book : books) {
                bookRepository.save(bookMapper.toEntity(book, true));
            }
            logger.info("Книги успешно импортированы.");
        } catch (HibernateException ex) {
            logger.error("Не удалось импортировать книги.");
            throw new HibernateException(ex);
        }
    }

    @Transactional
    public void exportBooksIntoCSVFile(String filename) throws IOException, HibernateException {
        logger.info("Экспорт книг.");
        try {
            fileManager.exportBooksIntoCSVFile(filename, bookMapper.toModelsList(bookRepository.findAll()));
            logger.info("Книги успешно экспортированы.");
        } catch (HibernateException ex) {
            logger.error("Не удалось экспортировать книги.");
            throw new HibernateException(ex);
        }
    }
}
