package com.yourcompany.controllers;

import com.yourcompany.DTO.RequestDTO;
import com.yourcompany.exceptions.BookNotFoundException;
import com.yourcompany.exceptions.RequestException;
import com.yourcompany.mappers.RequestMapper;
import com.yourcompany.models.Book;
import com.yourcompany.models.Request;
import com.yourcompany.security.MyUserDetailsService;
import com.yourcompany.services.RequestService;
import com.yourcompany.utils.JwtHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RequestController.class)
@AutoConfigureMockMvc(addFilters = false)
class RequestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private RequestService requestService;
    @MockitoBean
    private RequestMapper requestMapper;
    @MockitoBean
    JwtHandler jwtHandler;
    @MockitoBean
    MyUserDetailsService userDetailsService;

    private final List<RequestDTO> requestDTOS = new ArrayList<>();
    private RequestDTO requestDTO;
    private Request request;
    private Book book;

    @BeforeEach
    void setUp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        requestDTO = new RequestDTO(1L, "sdf", 3, true);
        requestDTOS.clear();
        requestDTOS.add(requestDTO);
        request = new Request(1L, book, 3, true);
        book = new Book(1,
                "sdf",
                "S. D. F.",
                "Book",
                Date.from(LocalDate.parse("2005-04-12", formatter).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                5,
                449.99
        );
    }

    @Nested
    class GetRequestsTestClass {

        @Test
        void getRequestsSuccessfullyTest() throws Exception {
            when(requestService.getRequests()).thenReturn(new ArrayList<>());
            when(requestMapper.toDTOList(any())).thenReturn(requestDTOS);

            mockMvc.perform(get("/request"))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].bookName").value("sdf"))
                    .andExpect(jsonPath("$[0].count").value(3))
                    .andExpect(jsonPath("$[0].open").value(true));
        }
    }

    @Nested
    class GetBookRequestsTestClass {

        @Test
        void getBookRequestsSuccessfullyTest() throws Exception {
            when(requestService.getBookRequests("sdf")).thenReturn(new ArrayList<>());
            when(requestMapper.toDTOList(any())).thenReturn(requestDTOS);

            mockMvc.perform(get("/request/{book_name}", "sdf"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].bookName").value("sdf"));
        }

        @Test
        void getBookRequestsBookNotFoundTest() throws Exception {
            doThrow(new BookNotFoundException("Book not found"))
                    .when(requestService).getBookRequests("unknown");

            mockMvc.perform(get("/request/{book_name}", "unknown"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class MakeRequestTestClass {

        @Test
        void makeRequestSuccessfullyTest() throws Exception {
            String requestJson = objectMapper.writeValueAsString(requestDTO);

            when(requestService.makeRequest(anyString(), anyInt())).thenReturn(request);

            mockMvc.perform(post("/request")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isOk());
        }

        @Test
        void makeRequestBookNotFoundExceptionTest() throws Exception {
            String requestJson = objectMapper.writeValueAsString(requestDTO);

            doThrow(new BookNotFoundException("Book not found"))
                    .when(requestService).makeRequest(anyString(), anyInt());

            mockMvc.perform(post("/request")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isNotFound());
        }

        @Test
        void makeRequestExceptionTest() throws Exception {
            String requestJson = objectMapper.writeValueAsString(requestDTO);

            doThrow(new RequestException("Invalid request data"))
                    .when(requestService).makeRequest(anyString(), anyInt());

            mockMvc.perform(post("/request")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class ImportRequestsTestClass {

        @Test
        void importRequestsSuccessfullyTest() throws Exception {
            doNothing().when(requestService).importRequestsFromCSVFile(anyString());

            mockMvc.perform(get("/request/import"))
                    .andExpect(status().isOk());
        }

        @Test
        void importRequestsExceptionTest() throws Exception {
            doThrow(new IOException("CSV file not found"))
                    .when(requestService).importRequestsFromCSVFile(anyString());

            mockMvc.perform(get("/request/import"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class ExportRequestsTestClass {

        @Test
        void exportRequestsSuccessfullyTest() throws Exception {
            doNothing().when(requestService).exportRequestsIntoCSVFile(anyString());

            mockMvc.perform(get("/request/export"))
                    .andExpect(status().isOk());
        }

        @Test
        void exportRequestsExceptionTest() throws Exception {
            doThrow(new IOException("Error writing to CSV file"))
                    .when(requestService).exportRequestsIntoCSVFile(anyString());

            mockMvc.perform(get("/request/export"))
                    .andExpect(status().isBadRequest());
        }
    }
}