package com.yourcompany.services;

import com.yourcompany.entities.BookEntity;
import com.yourcompany.mappers.BookMapper;
import com.yourcompany.mappers.ClientMapper;
import com.yourcompany.mappers.OrderMapper;
import com.yourcompany.mappers.RequestMapper;
import com.yourcompany.models.Book;
import com.yourcompany.repositories.BookRepository;
import com.yourcompany.repositories.ClientRepository;
import com.yourcompany.repositories.OrderRepository;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private RequestRepository requestRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private ClientMapper clientMapper;
    @Mock
    private RequestMapper requestMapper;
    @Mock
    FileManager fileManager;
    @InjectMocks
    OrderService orderService;

    //Data
    BookEntity bookEntity;
    Book book;

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
    }

    @Nested
    class GetOrdersTestClass {
        @Test
        public void getOrdersSuccessfullyTest() {
            
        }
    }
}