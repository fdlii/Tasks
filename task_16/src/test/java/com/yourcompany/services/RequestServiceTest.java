package com.yourcompany.services;

import com.yourcompany.entities.BookEntity;
import com.yourcompany.entities.RequestEntity;
import com.yourcompany.exceptions.BookNotFoundException;
import com.yourcompany.exceptions.RequestException;
import com.yourcompany.mappers.BookMapper;
import com.yourcompany.mappers.RequestMapper;
import com.yourcompany.models.Book;
import com.yourcompany.models.Request;
import com.yourcompany.repositories.BookRepository;
import com.yourcompany.repositories.RequestRepository;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class RequestServiceTest {
    @Mock
    RequestRepository requestRepository;
    @Mock
    RequestMapper requestMapper;
    @Mock
    BookRepository bookRepository;
    @Mock
    BookMapper bookMapper;
    @Mock
    FileManager fileManager;
    @InjectMocks
    RequestService requestService;

    //Data
    Book book;
    BookEntity bookEntity;
    Request request;
    RequestEntity requestEntity;
    List<Request> requests = new ArrayList<>();
    List<RequestEntity> requestEntities = new ArrayList<>();

    @BeforeEach
    public void setUpData() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        book = new Book(1,
                "sdf",
                "S. D. F.",
                "Book",
                Date.from(LocalDate.parse("2005-04-12", formatter).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                5,
                449.99
        );
        bookEntity = new BookEntity(1,
                "sdf",
                "S. D. F.",
                "Book",
                LocalDateTime.of(2005, 4, 12, 0, 0, 0),
                true,
                5,
                449.99);
        request = new Request(book, 2);
        requests.add(request);
        requestEntity = new RequestEntity(bookEntity, 2, request.isOpen());
        requestEntities.add(requestEntity);
    }

    @Nested
    class MakeRequestTestClass {
        @Test
        public void makeRequestSuccessfullyTest() throws BookNotFoundException, RequestException {
            //Given
            String name = "sdf";

            //When
            when(bookRepository.findByName(name)).thenReturn(bookEntity);
            when(bookMapper.toModel(bookEntity)).thenReturn(book);
            when(requestMapper.toEntity(any(Request.class), eq(true))).thenReturn(requestEntity);
            when(requestRepository.save(requestEntity)).thenReturn(requestEntity);

            //Then
            Request returned = requestService.makeRequest(name, 2);
            assertEquals(returned.getCount(), request.getCount());
            assertEquals(returned.getBook().getName(), book.getName());
            verify(requestRepository, times(1)).save(any(RequestEntity.class));
        }

        @Test
        public void makeRequestExceptionTest() {
            //Given
            String name = null;

            //Then
            RequestException exception = assertThrows(RequestException.class, () -> {
                Request returned = requestService.makeRequest(name, 2);
            });
            assertEquals("Некорректные параметры запроса.", exception.getMessage());
        }
    }

    @Nested
    class GetRequestsTestClass {
        @Test
        public void getRequestsSuccessfullyTest() {
            //When
            when(requestRepository.findAll()).thenReturn(requestEntities);
            when(requestMapper.toModelsList(requestEntities)).thenReturn(requests);

            //Then
            List<Request> returned = requestService.getRequests();
            assertEquals(requests.get(0).getCount(), returned.get(0).getCount());
            assertEquals(requests.get(0).getBook().getName(), returned.get(0).getBook().getName());
            verify(requestRepository, times(1)).findAll();
        }

        @Test
        public void getBookRequestsSuccessfullyTest() throws BookNotFoundException {
            //Given
            String name = "sdf";

            //When
            when(bookRepository.findByName(name)).thenReturn(bookEntity);
            when(bookMapper.toModel(bookEntity)).thenReturn(book);
            when(bookMapper.toEntity(book, false)).thenReturn(bookEntity);
            when(requestRepository.findByBook(bookEntity)).thenReturn(requestEntities);
            when(requestMapper.toModelsList(requestEntities)).thenReturn(requests);

            //Then
            List<Request> returned = requestService.getBookRequests(name);
            assertEquals(requests.get(0).getCount(), returned.get(0).getCount());
            assertEquals(requests.get(0).getBook().getName(), returned.get(0).getBook().getName());
            verify(requestRepository, times(1)).findByBook(any(BookEntity.class));
        }

        @Test
        public void getBookRequestsExceptionTest() {
            //Given
            String name = "";

            //When
            when(bookRepository.findByName(name)).thenReturn(null);
            when(bookMapper.toModel(null)).thenThrow(new NullPointerException("Не удалось найти запрашиваемую книгу."));

            //Then
            BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> {
                requestService.getBookRequests(name);
            });
            assertEquals("Не удалось найти запрашиваемую книгу.", exception.getMessage());
        }
    }

    @Nested
    class ImportRequestsTestClass {
        @Test
        public void importRequestsSuccessfullyTest() throws IOException {
            //Given
            String filename = "task_6/src/main/java/com/yourcompany/task_6_1/Requests.csv";
            List<Request> requests1 = new ArrayList<>();
            List<BookEntity> bookEntities = List.of(bookEntity);
            List<Book> books = List.of(book);

            //When
            when(bookRepository.findAll()).thenReturn(bookEntities);
            when(bookMapper.toModelsList(bookEntities)).thenReturn(books);
            when(requestMapper.toEntity(any(Request.class), eq(true))).thenReturn(requestEntity);
            when(requestRepository.save(requestEntity)).thenReturn(requestEntity);
            doNothing()
                    .when(fileManager)
                    .importRequestsFromCSVFile(filename, requests1, books);

            //Then
            assertDoesNotThrow(() -> {
                requestService.importRequestsFromCSVFile(filename);
            });
        }

        @Test
        public void importRequestsExceptionTest() throws IOException {
            //Given
            String filename = "";
            List<Request> requests1 = new ArrayList<>();
            List<BookEntity> bookEntities = List.of(bookEntity);
            List<Book> books = List.of(book);

            //When
            when(bookRepository.findAll()).thenReturn(bookEntities);
            when(bookMapper.toModelsList(bookEntities)).thenReturn(books);
            doThrow(new IOException("Не удалось прочитать файл."))
                    .when(fileManager)
                    .importRequestsFromCSVFile(filename, requests1, books);

            //Then
            IOException exception = assertThrows(IOException.class, () -> {
                requestService.importRequestsFromCSVFile(filename);
            });
            assertEquals("Не удалось прочитать файл.", exception.getMessage());
        }
    }

    @Nested
    class ExportRequestsTestClass {
        @Test
        public void exportRequestsSuccessfullyTest() throws IOException {
            //Given
            String filename = "task_6/src/main/java/com/yourcompany/task_6_1/Requests.csv";
            //When
            doNothing()
                    .when(fileManager)
                    .exportRequestsIntoCSVFile(filename, requests);
            when(requestMapper.toModelsList(any(List.class)))
                    .thenReturn(requests);
            when(requestRepository.findAll())
                    .thenReturn(new ArrayList<RequestEntity>());

            //Then
            assertDoesNotThrow(() -> {
                requestService.exportRequestsIntoCSVFile(filename);
            });
            verify(requestRepository, times(1)).findAll();
        }

        @Test
        public void exportRequestsExceptionTest() throws IOException {
            //Given
            String filename = "";

            //When
            doThrow(new IOException("Не удалось прочитать файл."))
                    .when(fileManager)
                    .exportRequestsIntoCSVFile(filename, requests);
            when(requestMapper.toModelsList(any(List.class)))
                    .thenReturn(requests);
            when(requestRepository.findAll())
                    .thenReturn(new ArrayList<RequestEntity>());

            //Then
            IOException exception = assertThrows(IOException.class, () -> {
                requestService.exportRequestsIntoCSVFile(filename);
            });
            assertEquals("Не удалось прочитать файл.", exception.getMessage());
        }
    }
}