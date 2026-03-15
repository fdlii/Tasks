package com.yourcompany.services;

import com.yourcompany.mappers.BookMapper;
import com.yourcompany.mappers.RequestMapper;
import com.yourcompany.models.Book;
import com.yourcompany.repositories.BookRepository;
import com.yourcompany.repositories.RequestRepository;
import com.yourcompany.utils.FileManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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
    }
}