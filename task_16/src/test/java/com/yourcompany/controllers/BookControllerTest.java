package com.yourcompany.controllers;

import com.yourcompany.DTO.BookDTO;
import com.yourcompany.exceptions.BookException;
import com.yourcompany.exceptions.BookNotFoundException;
import com.yourcompany.mappers.BookMapper;
import com.yourcompany.models.Book;
import com.yourcompany.security.MyUserDetailsService;
import com.yourcompany.services.BookService;
import com.yourcompany.utils.JwtHandler;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebMvcTest(BookController.class)
class BookControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockitoBean
    BookService bookService;
    @MockitoBean
    BookMapper bookMapper;
    @MockitoBean
    JwtHandler jwtHandler;
    @MockitoBean
    MyUserDetailsService userDetailsService;

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
        bookDTOS.add(bookDTO);
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
        public void getStaledBooksSuccessfullyTest() throws Exception {
            //When
            when(bookMapper.toDTOList(books)).thenReturn(bookDTOS);
            when(bookService.getStaledBooks()).thenReturn(books);

            //Then
            mockMvc.perform(get("/book/staled"))
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].id").value(1L))
                    .andExpect(jsonPath("$[0].name").value("sdf"))
                    .andExpect(jsonPath("$[0].author").value("S. D. F."));
        }
    }

    @Nested
    class AddBookTestClass {

        @Test
        void addBookSuccessfullyTest() throws Exception {
            String requestJson = objectMapper.writeValueAsString(bookDTO);

            doNothing().when(bookService).addBook(anyString(), anyString(), anyString(), any(Date.class), anyDouble(), anyInt());

            mockMvc.perform(post("/book")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.name").value("sdf"))
                    .andExpect(jsonPath("$.author").value("S. D. F."));
        }

        @Test
        void addBookExceptionTest() throws Exception {
            String requestJson = objectMapper.writeValueAsString(bookDTO);

            doThrow(new BookException("Book already exists"))
                    .when(bookService).addBook(anyString(), anyString(), anyString(), any(Date.class), anyDouble(), anyInt());

            mockMvc.perform(post("/book")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class DeleteBookTestClass {

        @Test
        void deleteBookSuccessfullyTest() throws Exception {
            doNothing().when(bookService).deleteBook("sdf");

            mockMvc.perform(delete("/book/{book_name}", "sdf"))
                    .andExpect(status().isOk());
        }

        @Test
        void deleteBookExceptionTest() throws Exception {
            doThrow(new BookNotFoundException("Book not found"))
                    .when(bookService).deleteBook("unknown");

            mockMvc.perform(delete("/book/{book_name}", "unknown"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class AddBookInStockTestClass {

        @Test
        void addBookInStockSuccessfullyTest() throws Exception {
            when(bookService.addInStock("sdf", 10)).thenReturn(book);

            mockMvc.perform(put("/book/{book_name}/{count}", "sdf", 10))
                    .andExpect(status().isOk());
        }

        @Test
        void addBookInStockExceptionTest() throws Exception {
            doThrow(new BookNotFoundException("Book not found"))
                    .when(bookService).addInStock("unknown", 5);

            mockMvc.perform(put("/book/{book_name}/{count}", "unknown", 5))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class DebitBookFromStockTestClass {

        @Test
        void debitBookFromStockSuccessfullyTest() throws Exception {
            when(bookService.debitFromStock("sdf")).thenReturn(book);

            mockMvc.perform(put("/book/{book_name}", "sdf"))
                    .andExpect(status().isOk());
        }

        @Test
        void debitBookFromStockExceptionTest() throws Exception {
            doThrow(new BookNotFoundException("Book not found"))
                    .when(bookService).debitFromStock("unknown");

            mockMvc.perform(put("/book/{book_name}", "unknown"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class ImportBooksTestClass {

        @Test
        void importBooksSuccessfullyTest() throws Exception {
            doNothing().when(bookService).importBooksFromCSVFile(anyString());

            mockMvc.perform(get("/book/import"))
                    .andExpect(status().isOk());
        }

        @Test
        void importBooksExceptionTest() throws Exception {
            doThrow(new IOException("File not found"))
                    .when(bookService).importBooksFromCSVFile(anyString());

            mockMvc.perform(get("/book/import"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class ExportBooksTestClass {

        @Test
        void exportBooksSuccessfullyTest() throws Exception {
            doNothing().when(bookService).exportBooksIntoCSVFile(anyString());

            mockMvc.perform(get("/book/export"))
                    .andExpect(status().isOk());
        }

        @Test
        void exportBooksExceptionTest() throws Exception {
            doThrow(new IOException("Write error"))
                    .when(bookService).exportBooksIntoCSVFile(anyString());

            mockMvc.perform(get("/book/export"))
                    .andExpect(status().isBadRequest());
        }
    }
}