package com.yourcompany.controllers;

import com.yourcompany.DTO.OrderDTO;
import com.yourcompany.exceptions.OrderException;
import com.yourcompany.exceptions.OrderNotFoundException;
import com.yourcompany.mappers.OrderMapper;
import com.yourcompany.models.Book;
import com.yourcompany.models.Client;
import com.yourcompany.models.Order;
import com.yourcompany.models.OrderStatus;
import com.yourcompany.security.MyUserDetailsService;
import com.yourcompany.services.OrderService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private OrderService orderService;
    @MockitoBean
    private OrderMapper orderMapper;
    @MockitoBean
    JwtHandler jwtHandler;
    @MockitoBean
    MyUserDetailsService userDetailsService;

    private List<OrderDTO> orderDTOS = new ArrayList<>();
    private OrderDTO orderDTO;
    private Order order;
    List<Order> orders = new ArrayList<>();
    Client client;
    Book book;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @BeforeEach
    void setUp() {
        orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        orderDTO.setClientName("Иван Иванов");
        orderDTO.setDiscount(0.1);
        orderDTO.setFinalPrice(899.99);
        orderDTO.setExecutionDate(Date.from(LocalDate.parse("2025-04-15", formatter)
                .atStartOfDay(ZoneId.systemDefault()).toInstant()));
        orderDTO.setOrderStatus(OrderStatus.COMPLETED);
        orderDTO.bookNames = List.of("sdf", "Another Book");
        orderDTOS.add(orderDTO);

        client = new Client(1, "Nikita", 25);
        book = new Book(1,
                "sdf",
                "S. D. F.",
                "Book",
                Date.from(LocalDate.parse("2005-04-12", formatter).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                5,
                449.99
        );

        order = new Order(
                1,
                client,
                0,
                449.99,
                Date.from(LocalDate.parse("2026-03-12", formatter).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                OrderStatus.COMPLETED
        );
        order.setBooks(List.of(book));
        orders.add(order);
    }

    @Nested
    class GetOrdersTestClass {

        @Test
        void getOrdersSuccessfullyTest() throws Exception {
            when(orderService.getOrders()).thenReturn(new ArrayList<>());
            when(orderMapper.toDTOList(any())).thenReturn(orderDTOS);

            mockMvc.perform(get("/order"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].clientName").value("Иван Иванов"))
                    .andExpect(jsonPath("$[0].finalPrice").value(899.99));
        }
    }

    @Nested
    class GetOrderByIdTestClass {

        @Test
        void getOrderByIdSuccessfullyTest() throws Exception {
            when(orderService.getOrderById(1L)).thenReturn(order);
            when(orderMapper.fromModelToDTO(any())).thenReturn(orderDTO);

            mockMvc.perform(get("/order/{id}", 1L))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.clientName").value("Иван Иванов"));
        }

        @Test
        void getOrderByIdNotFoundTest() throws Exception {
            doThrow(new OrderNotFoundException("Order not found"))
                    .when(orderService).getOrderById(999L);

            mockMvc.perform(get("/order/{id}", 999L))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class GetEarnedFundsTestClass {

        @Test
        void getEarnedFundsSuccessfullyTest() throws Exception {
            when(orderService.getEarnedFundsForTimeSpan(any(Date.class), any(Date.class)))
                    .thenReturn(12500.75);

            mockMvc.perform(get("/order/get_funds")
                            .param("from", "2025-01-01")
                            .param("to", "2025-12-31"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("12500.75"));
        }
    }

    @Nested
    class GetCompletedOrdersCountTestClass {

        @Test
        void getCompletedOrdersCountSuccessfullyTest() throws Exception {
            when(orderService.getCompletedOrdersCountForTimeSpan(any(Date.class), any(Date.class)))
                    .thenReturn(42L);

            mockMvc.perform(get("/order/get_orders_count")
                            .param("from", "2025-01-01")
                            .param("to", "2025-12-31"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("42"));
        }
    }

    @Nested
    class GetCompletedOrdersTestClass {

        @Test
        void getCompletedOrdersSuccessfullyTest() throws Exception {
            when(orderService.getCompletedOrdersForTimeSpan(any(Date.class), any(Date.class)))
                    .thenReturn(new ArrayList<>());
            when(orderMapper.toDTOList(any())).thenReturn(orderDTOS);

            mockMvc.perform(get("/order/get_orders")
                            .param("from", "2025-01-01")
                            .param("to", "2025-12-31"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(1));
        }
    }

    @Nested
    class CreateOrderTestClass {

        @Test
        void createOrderSuccessfullyTest() throws Exception {
            String requestJson = objectMapper.writeValueAsString(orderDTO);

            when(orderService.createOrder(
                    anyDouble(), any(Date.class), anyString(), anyList())).thenReturn(order);

            mockMvc.perform(post("/order")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isOk());
        }

        @Test
        void createOrderExceptionTest() throws Exception {
            String requestJson = objectMapper.writeValueAsString(orderDTO);

            doThrow(new OrderException("Cannot create order"))
                    .when(orderService).createOrder(anyDouble(), any(Date.class), anyString(), anyList());

            mockMvc.perform(post("/order")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class UpdateOrderTestClass {

        @Test
        void updateOrderSuccessfullyTest() throws Exception {
            order.changeStatus(OrderStatus.COMPLETED);
            when(orderService.updateOrder(1L, OrderStatus.COMPLETED)).thenReturn(order);

            mockMvc.perform(put("/order/{id}/{status}", 1L, OrderStatus.COMPLETED))
                    .andExpect(status().isOk());
        }

        @Test
        void updateOrderNotFoundTest() throws Exception {
            doThrow(new OrderNotFoundException("Order not found"))
                    .when(orderService).updateOrder(999L, OrderStatus.COMPLETED);

            mockMvc.perform(put("/order/{id}/{status}", 999L, OrderStatus.COMPLETED))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class ImportOrdersTestClass {

        @Test
        void importOrdersSuccessfullyTest() throws Exception {
            doNothing().when(orderService).importOrdersFromCSVFile(anyString());

            mockMvc.perform(get("/order/import"))
                    .andExpect(status().isOk());
        }

        @Test
        void importOrdersExceptionTest() throws Exception {
            doThrow(new IOException("CSV file not found"))
                    .when(orderService).importOrdersFromCSVFile(anyString());

            mockMvc.perform(get("/order/import"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class ExportOrdersTestClass {

        @Test
        void exportOrdersSuccessfullyTest() throws Exception {
            doNothing().when(orderService).exportOrdersIntoCSVFile(anyString());

            mockMvc.perform(get("/order/export"))
                    .andExpect(status().isOk());
        }

        @Test
        void exportOrdersExceptionTest() throws Exception {
            doThrow(new IOException("Error writing CSV"))
                    .when(orderService).exportOrdersIntoCSVFile(anyString());

            mockMvc.perform(get("/order/export"))
                    .andExpect(status().isBadRequest());
        }
    }
}