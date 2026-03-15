package com.yourcompany.services;

import com.yourcompany.comparators.BookIsInStockComparator;
import com.yourcompany.comparators.BookNameComparator;
import com.yourcompany.comparators.BookPriceComparator;
import com.yourcompany.comparators.BookPublishedComparator;
import com.yourcompany.exceptions.BookException;
import com.yourcompany.exceptions.BookNotFoundException;
import com.yourcompany.mappers.BookMapper;
import com.yourcompany.models.Book;
import com.yourcompany.repositories.BookRepository;
import com.yourcompany.utils.FileManager;
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
    public void addBook(String name, String author, String description, Date published, double price, int countInStock) throws BookException {
        logger.info("Добавление книги.");
        if (name == null || name.isEmpty() || price <= 0 || countInStock < 0) {
            logger.error("Некорректные параметры книги.");
            throw new BookException("Некорректные параметры книги для добавления.");
        }
        bookRepository.save(bookMapper.toEntity(new Book(name, author, description, published, price, countInStock), true));
        logger.info("Книга успешно добавлена.");
    }

    @Transactional
    public void deleteBook(String bookName) throws BookNotFoundException {
        logger.info("Удаление книги.");
        try {
            Book book = bookMapper.toModel(bookRepository.findByName(bookName));
            bookRepository.delete(bookMapper.toEntity(book, false));
            logger.info("Книга успешно удалена.");
        }
        catch (NullPointerException ex) {
            logger.error(ex.getMessage());
            throw new BookNotFoundException(ex.getMessage());
        }
    }

    @Transactional
    public List<Book> getBooks() {
        logger.info("Получение всех книг.");
        List<Book> books = bookMapper.toModelsList(bookRepository.findAll());
        logger.info("Книги успешно получены.");

        books.sort(new BookNameComparator()
                .thenComparing(new BookPublishedComparator()
                        .thenComparing(new BookPriceComparator()
                                .thenComparing(new BookIsInStockComparator()))));
        return books;
    }

    @Transactional
    public List<Book> getStaledBooks() throws HibernateException {
        logger.info("Получение залежавшихся книг.");
        LocalDateTime threshold = LocalDateTime.now().minusMonths(6);

        List<Book> staledBooks = bookMapper.toModelsList(bookRepository.getStaledBooks(threshold));
        logger.info("Книги успешно получены.");

        staledBooks.sort(new BookPublishedComparator()
                .thenComparing(new BookPriceComparator()));
        return staledBooks;
    }

    @Transactional
    public Book addInStock(String bookName, int count) throws BookNotFoundException {
        logger.info("Добавление книги на склад.");
        try {
            Book book = bookMapper.toModel(bookRepository.findByName(bookName));
            book.setCountInStock(book.getCountInStock() + count);
            bookRepository.save(bookMapper.toEntity(book, false));
            logger.info("Книга успешно добавлена на склад.");
            return book;
        }
        catch (NullPointerException ex) {
            logger.error(ex.getMessage());
            throw new BookNotFoundException(ex.getMessage());
        }
    }

    @Transactional
    public Book debitFromStock(String bookName) throws BookNotFoundException {
        logger.info("Удаление книги со склада.");
        try {
            Book book = bookMapper.toModel(bookRepository.findByName(bookName));
            book.setCountInStock(0);
            book.setInStock(false);
            bookRepository.save(bookMapper.toEntity(book, false));
            logger.info("Книга успешно удалена со склада.");
            return book;
        }
        catch (NullPointerException ex) {
            logger.error(ex.getMessage());
            throw new BookNotFoundException(ex.getMessage());
        }
    }

    @Transactional
    public void importBooksFromCSVFile(String filename) throws IOException {
        logger.info("Импорт книг.");
        List<Book> books = new ArrayList<>();
        fileManager.importBooksFromCSVFile(filename, books);
        for (Book book : books) {
            bookRepository.save(bookMapper.toEntity(book, true));
        }
        logger.info("Книги успешно импортированы.");
    }

    @Transactional
    public void exportBooksIntoCSVFile(String filename) throws IOException {
        logger.info("Экспорт книг.");
        fileManager.exportBooksIntoCSVFile(filename, bookMapper.toModelsList(bookRepository.findAll()));
        logger.info("Книги успешно экспортированы.");
    }
}
