package com.yourcompany.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourcompany.DTO.BookDTO;
import com.yourcompany.config.SecurityConfig;
import com.yourcompany.exceptions.BookNotFoundException;
import com.yourcompany.mappers.BookMapper;
import com.yourcompany.models.Book;
import com.yourcompany.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.autoconfigure.DispatcherServletAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(BookController.class)
class BookControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    BookService bookService;
    @MockitoBean
    BookMapper bookMapper;

    //Data
    List<Book> books = new ArrayList<>();
    List<BookDTO> bookDTOS= new ArrayList<>();
    BookDTO bookDTO;
    Book book;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @BeforeEach
    public void setUpData() {
        book = new Book(1,
                "sdf",
                "S. D. F.",
                "Book",
                Date.from(LocalDate.parse("2005-04-12", formatter).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                5,
                449.99
        );
        books.add(book);
        bookDTO= new BookDTO(1,
                "sdf",
                "S. D. F.",
                "Book",
                Date.from(LocalDate.parse("2005-04-12", formatter).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                true,
                5,
                449.99);
    }

    @Nested
    class GetBooksTestClass {
        @Test
        public void getBooksSuccessfullyTest() throws Exception {
            //When
            when(bookMapper.toDTOList(books)).thenReturn(bookDTOS);
            when(bookService.getBooks()).thenReturn(books);

            //Then
            mockMvc.perform(get("/book"))
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].id").value(1L))
                    .andExpect(jsonPath("$[0].name").value("sdf"))
                    .andExpect(jsonPath("$[0].author").value("S. D. F."));
        }

        @Test
        public void getStaledBooksSuccessfullyTest() {

        }
    }

    @Nested
    class AddBookTestClass {
        @Test
        public void addBookSuccessfullyTest() {

        }

        @Test
        public void addBookExceptionTest() {

        }
    }

    @Nested
    class DeleteBookTestClass {
        @Test
        public void deleteBookSuccessfullyTest() {

        }

        @Test
        public void deleteBookExceptionTest() {

        }
    }

    @Nested
    class AddBookInStockTestClass {
        @Test
        public void addBookInStockSuccessfullyTest() throws BookNotFoundException {

        }

        @Test
        public void addBookInStockExceptionTest() {

        }
    }

    @Nested
    class DebitBookFromStockTestClass {
        @Test
        public void debitBookFromStockSuccessfullyTest() throws BookNotFoundException {

        }

        @Test
        public void debitBookFromStockExceptionTest() {

        }
    }

    @Nested
    class ImportBooksTestClass {
        @Test
        public void importBooksSuccessfullyTest() throws IOException {

        }

        @Test
        public void importBooksExceptionTest() throws IOException {

        }
    }

    @Nested
    class ExportBooksTestClass {
        @Test
        public void exportBooksSuccessfullyTest() throws IOException {

        }

        @Test
        public void exportBooksExceptionTest() throws IOException {

        }
    }
}