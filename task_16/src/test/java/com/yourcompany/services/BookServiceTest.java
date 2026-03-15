package com.yourcompany.services;

import com.yourcompany.entities.BookEntity;
import com.yourcompany.entities.ClientEntity;
import com.yourcompany.exceptions.BookException;
import com.yourcompany.exceptions.BookNotFoundException;
import com.yourcompany.mappers.BookMapper;
import com.yourcompany.models.Book;
import com.yourcompany.models.Client;
import com.yourcompany.repositories.BookRepository;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    BookRepository bookRepository;
    @Mock
    BookMapper bookMapper;
    @Mock
    FileManager fileManager;
    @InjectMocks
    BookService bookService;

    //Data
    List<Book> books = new ArrayList<>();
    BookEntity bookEntity;
    Book book;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @BeforeEach
    public void setUpData() throws ParseException {
        book = new Book(1,
                "sdf",
                "S. D. F.",
                "Book",
                Date.from(LocalDate.parse("2005-04-12", formatter).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                5,
                449.99
        );
        books.add(book);
        bookEntity = new BookEntity(1,
                "sdf",
                "S. D. F.",
                "Book",
                LocalDateTime.of(2005, 4, 12, 0, 0, 0),
                true,
                5,
                449.99);
    }

    @Nested
    class GetBooksTestClass {
        @Test
        public void getBooksSuccessfullyTest() throws ParseException {
            //Given
            List<BookEntity> bookEntities = List.of(
                bookEntity
            );

            //When
            when(bookRepository.findAll()).thenReturn(bookEntities);
            when(bookMapper.toModelsList(bookEntities)).thenReturn(books);

            //Then
            List<Book> books1 = bookService.getBooks();
            assertEquals(books1.get(0).getName(), books.get(0).getName());
            assertEquals(books1.get(0).getPublished(), books.get(0).getPublished());
            verify(bookRepository, times(1)).findAll();
        }

        @Test
        public void getStaledBooksSuccessfullyTest() {
            //Given
            List<BookEntity> bookEntities = List.of(
                    new BookEntity(1, "sdf", "S. D. F.", "Book", LocalDateTime.of(2005, 4, 12, 0, 0, 0), true,5, 449.99)
            );

            //When
            when(bookRepository.getStaledBooks(any(LocalDateTime.class))).thenReturn(bookEntities);
            when(bookMapper.toModelsList(bookEntities)).thenReturn(books);

            //Then
            List<Book> books1 = bookService.getStaledBooks();
            assertEquals(books1.get(0).getName(), books.get(0).getName());
            assertEquals(books1.get(0).getPublished(), books.get(0).getPublished());
            verify(bookRepository, times(1)).getStaledBooks(any(LocalDateTime.class));
        }
    }

    @Nested
    class AddBookTestClass {
        @Test
        public void addBookSuccessfullyTest() {
            //When
            when(bookMapper.toEntity(any(Book.class), eq(true))).thenReturn(bookEntity);
            when(bookRepository.save(any(BookEntity.class))).thenReturn(bookEntity);

            //Then
            assertDoesNotThrow(() -> {
                bookService.addBook(
                        "sdf",
                        "S. D. F.",
                        "Book",
                        Date.from(LocalDate.parse("2005-04-12", formatter).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        449.99,
                        5
                );
            });
            verify(bookRepository, times(1)).save(any(BookEntity.class));
        }

        @Test
        public void addBookExceptionTest() {
            //Then
            BookException exception = assertThrows(BookException.class, () -> {
                bookService.addBook(
                        null,
                        "S. D. F.",
                        "Book",
                        Date.from(LocalDate.parse("2005-04-12", formatter).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        449.99,
                        5
                );
            });
            assertEquals("Некорректные параметры книги для добавления.", exception.getMessage());
        }
    }

    @Nested
    class DeleteBookTestClass {
        @Test
        public void deleteBookSuccessfullyTest() {
            //Given
            String name = "sdf";

            //When
            when(bookRepository.findByName(name)).thenReturn(bookEntity);
            when(bookMapper.toModel(bookEntity)).thenReturn(book);
            when(bookMapper.toEntity(book, false)).thenReturn(bookEntity);
            doNothing().when(bookRepository).delete(bookEntity);

            //Then
            assertDoesNotThrow(() -> {
                bookService.deleteBook(name);
            });
            verify(bookRepository, times(1)).findByName(any(String.class));
            verify(bookRepository, times(1)).delete(any(BookEntity.class));
        }

        @Test
        public void deleteBookExceptionTest() {
            //Given
            String name = "";

            //When
            when(bookRepository.findByName(name)).thenReturn(null);
            when(bookMapper.toModel(null)).thenThrow(new NullPointerException("Не удалось найти запрашиваемую книгу."));

            //Then
            BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> {
                bookService.deleteBook(name);
            });
            assertEquals("Не удалось найти запрашиваемую книгу.", exception.getMessage());
        }
    }

    @Nested
    class AddBookInStockTestClass {
        @Test
        public void addBookInStockSuccessfullyTest() throws BookNotFoundException {
            //Given
            String name = "sdf";
            Book performedBook = new Book("sdf",
                    "S. D. F.",
                    "Book",
                    Date.from(LocalDate.parse("2005-04-12", formatter).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    449.99,
                    10
            );
            BookEntity performedBookEntity = new BookEntity("sdf",
                    "S. D. F.",
                    "Book",
                    LocalDateTime.of(2005, 4, 12, 0, 0, 0),
                    true,
                    10,
                    449.99
            );

            //When
            when(bookRepository.findByName(name)).thenReturn(bookEntity);
            when(bookMapper.toModel(bookEntity)).thenReturn(book);
            when(bookMapper.toEntity(any(Book.class), eq(false))).thenReturn(performedBookEntity);
            when(bookRepository.save(performedBookEntity)).thenReturn(performedBookEntity);

            //Then
            Book returned = bookService.addInStock(name, 5);
            assertEquals(returned.getCountInStock(), performedBook.getCountInStock());
            verify(bookRepository, times(1)).save(any(BookEntity.class));
            verify(bookRepository, times(1)).findByName(any(String.class));

        }

        @Test
        public void addBookInStockExceptionTest() {
            //Given
            String name = "";

            //When
            when(bookRepository.findByName(name)).thenReturn(null);
            when(bookMapper.toModel(null)).thenThrow(new NullPointerException("Не удалось найти запрашиваемую книгу."));

            //Then
            BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> {
                Book returned = bookService.addInStock(name, 5);
            });
            assertEquals("Не удалось найти запрашиваемую книгу.", exception.getMessage());
        }
    }

    @Nested
    class DebitBookFromStockTestClass {
        @Test
        public void debitBookFromStockSuccessfullyTest() throws BookNotFoundException {
            //Given
            String name = "sdf";
            Book performedBook = new Book("sdf",
                    "S. D. F.",
                    "Book",
                    Date.from(LocalDate.parse("2005-04-12", formatter).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    449.99,
                    0
            );
            BookEntity performedBookEntity = new BookEntity("sdf",
                    "S. D. F.",
                    "Book",
                    LocalDateTime.of(2005, 4, 12, 0, 0, 0),
                    false,
                    0,
                    449.99
            );

            //When
            when(bookRepository.findByName(name)).thenReturn(bookEntity);
            when(bookMapper.toModel(bookEntity)).thenReturn(book);
            when(bookMapper.toEntity(any(Book.class), eq(false))).thenReturn(performedBookEntity);
            when(bookRepository.save(performedBookEntity)).thenReturn(performedBookEntity);

            //Then
            Book returned = bookService.debitFromStock(name);
            assertEquals(returned.getCountInStock(), performedBook.getCountInStock());
            verify(bookRepository, times(1)).save(any(BookEntity.class));
            verify(bookRepository, times(1)).findByName(any(String.class));

        }

        @Test
        public void debitBookFromStockExceptionTest() {
            //Given
            String name = "";

            //When
            when(bookRepository.findByName(name)).thenReturn(null);
            when(bookMapper.toModel(null)).thenThrow(new NullPointerException("Не удалось найти запрашиваемую книгу."));

            //Then
            BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> {
                Book returned = bookService.debitFromStock(name);
            });
            assertEquals("Не удалось найти запрашиваемую книгу.", exception.getMessage());
        }
    }

    @Nested
    class ImportBooksTestClass {
        @Test
        public void importBooksSuccessfullyTest() throws IOException {
            //Given
            String filename = "task_6/src/main/java/com/yourcompany/task_6_1/Books.csv";
            List<Book> books = new ArrayList<>();

            //When
            doNothing()
                    .when(fileManager)
                    .importBooksFromCSVFile(filename, books);
            when(bookMapper.toEntity(any(Book.class), eq(true)))
                    .thenReturn(bookEntity);
            when(bookRepository.save(bookEntity))
                    .thenReturn(bookEntity);

            //Then
            assertDoesNotThrow(() -> {
                bookService.importBooksFromCSVFile(filename);
            });
        }

        @Test
        public void importBooksExceptionTest() throws IOException {
            //Given
            String filename = "";
            List<Book> books = new ArrayList<>();

            //When
            doThrow(new IOException("Не удалось прочитать файл."))
                    .when(fileManager)
                    .importBooksFromCSVFile(filename, books);

            //Then
            IOException exception = assertThrows(IOException.class, () -> {
                bookService.importBooksFromCSVFile(filename);
            });
            assertEquals("Не удалось прочитать файл.", exception.getMessage());
        }
    }

    @Nested
    class ExportBooksTestClass {
        @Test
        public void exportBooksSuccessfullyTest() throws IOException {
            //Given
            String filename = "task_6/src/main/java/com/yourcompany/task_6_1/Books.csv";
            //When
            doNothing()
                    .when(fileManager)
                    .exportBooksIntoCSVFile(filename, books);
            when(bookMapper.toModelsList(any(List.class)))
                    .thenReturn(books);
            when(bookRepository.findAll())
                    .thenReturn(new ArrayList<BookEntity>());

            //Then
            assertDoesNotThrow(() -> {
                bookService.exportBooksIntoCSVFile(filename);
            });
            verify(bookRepository, times(1)).findAll();
        }

        @Test
        public void exportBooksExceptionTest() throws IOException {
            //Given
            String filename = "";

            //When
            doThrow(new IOException("Не удалось прочитать файл."))
                    .when(fileManager)
                    .exportBooksIntoCSVFile(filename, books);
            when(bookMapper.toModelsList(any(List.class)))
                    .thenReturn(books);
            when(bookRepository.findAll())
                    .thenReturn(new ArrayList<BookEntity>());

            //Then
            IOException exception = assertThrows(IOException.class, () -> {
                bookService.exportBooksIntoCSVFile(filename);
            });
            assertEquals("Не удалось прочитать файл.", exception.getMessage());
        }
    }
}