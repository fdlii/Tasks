package com.yourcompany.controllers;

import com.yourcompany.DTO.ClientDTO;
import com.yourcompany.exceptions.ClientException;
import com.yourcompany.mappers.ClientMapper;
import com.yourcompany.models.Client;
import com.yourcompany.security.MyUserDetailsService;
import com.yourcompany.services.ClientService;
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
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
@AutoConfigureMockMvc(addFilters = false)
class ClientControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private ClientService clientService;
    @MockitoBean
    private ClientMapper clientMapper;
    @MockitoBean
    JwtHandler jwtHandler;
    @MockitoBean
    MyUserDetailsService userDetailsService;

    private List<ClientDTO> clientDTOS = new ArrayList<>();
    private ClientDTO clientDTO;

    @BeforeEach
    void setUp() {
        clientDTO = new ClientDTO(1L, "Иван Иванов", 25);

        clientDTOS.clear();
        clientDTOS.add(clientDTO);
    }

    @Nested
    class GetClientsTestClass {

        @Test
        void getClientsSuccessfullyTest() throws Exception {
            when(clientService.getClients()).thenReturn(new ArrayList<>()); // сервис возвращает модели
            when(clientMapper.toDTOList(any())).thenReturn(clientDTOS);

            mockMvc.perform(get("/client"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].name").value("Иван Иванов"))
                    .andExpect(jsonPath("$[0].age").value(25));
        }
    }

    @Nested
    class AddClientTestClass {

        @Test
        void addClientSuccessfullyTest() throws Exception {
            String requestJson = objectMapper.writeValueAsString(clientDTO);

            // Мокируем сервис и маппер
            when(clientService.addClient(anyString(), anyInt())).thenReturn(new Client(clientDTO.getName(), clientDTO.getAge())); // заглушка, т.к. мы не проверяем модель
            when(clientMapper.fromModelToDTO(any(Client.class))).thenReturn(clientDTO);

            mockMvc.perform(post("/client")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.name").value("Иван Иванов"))
                    .andExpect(jsonPath("$.age").value(25));
        }

        @Test
        void addClientExceptionTest() throws Exception {
            String requestJson = objectMapper.writeValueAsString(clientDTO);

            doThrow(new ClientException("Client with this name already exists"))
                    .when(clientService).addClient(anyString(), anyInt());

            mockMvc.perform(post("/client")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class ImportClientsTestClass {

        @Test
        void importClientsSuccessfullyTest() throws Exception {
            doNothing().when(clientService).importClientsFromCSVFile(anyString());

            mockMvc.perform(get("/client/import"))
                    .andExpect(status().isOk());
        }

        @Test
        void importClientsExceptionTest() throws Exception {
            doThrow(new IOException("CSV file not found"))
                    .when(clientService).importClientsFromCSVFile(anyString());

            mockMvc.perform(get("/client/import"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class ExportClientsTestClass {

        @Test
        void exportClientsSuccessfullyTest() throws Exception {
            doNothing().when(clientService).exportClientsIntoCSVFile(anyString());

            mockMvc.perform(get("/client/export"))
                    .andExpect(status().isOk());
        }

        @Test
        void exportClientsExceptionTest() throws Exception {
            doThrow(new IOException("Error writing CSV file"))
                    .when(clientService).exportClientsIntoCSVFile(anyString());

            mockMvc.perform(get("/client/export"))
                    .andExpect(status().isBadRequest());
        }
    }
}