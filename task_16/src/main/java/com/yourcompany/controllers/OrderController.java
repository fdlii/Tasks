package com.yourcompany.controllers;

import com.yourcompany.DTO.OrderDTO;
import com.yourcompany.exceptions.BookNotFoundException;
import com.yourcompany.exceptions.ClientNotFoundException;
import com.yourcompany.exceptions.OrderException;
import com.yourcompany.exceptions.OrderNotFoundException;
import com.yourcompany.mappers.OrderMapper;
import com.yourcompany.models.OrderStatus;
import com.yourcompany.services.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    OrderMapper orderMapper;

    @GetMapping
    public List<OrderDTO> getOrders() {
        return orderMapper.toDTOList(orderService.getOrders());
    }

    @GetMapping("/{id}")
    public OrderDTO getOrderById(@PathVariable("id") long id) throws OrderNotFoundException {
        return orderMapper.fromModelToDTO(orderService.getOrderById(id));
    }

    @GetMapping("/get_funds")
    public double getEarnedFundsForTimeSpan(
            @RequestParam("from") String from,
            @RequestParam("to")   String to
    )
    {
        Date fromD = null;
        Date toD   = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDate fromDate = LocalDate.parse(from, formatter);
            LocalDate toDate   = LocalDate.parse(to,   formatter);

            fromD = Date.from(fromDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            toD = Date.from(toDate  .atStartOfDay(ZoneId.systemDefault()).toInstant());
        } catch (Exception ex) {
            throw new IllegalArgumentException("Не удалось преобразовать переданные даты.");
        }
        return orderService.getEarnedFundsForTimeSpan(fromD, toD);
    }

    @GetMapping("/get_orders_count")
    public long getCompletedOrdersCountForTimeSpan(
            @RequestParam("from") String from,
            @RequestParam("to")   String to
    )
    {
        Date fromD = null;
        Date toD   = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDate fromDate = LocalDate.parse(from, formatter);
            LocalDate toDate   = LocalDate.parse(to,   formatter);

            fromD = Date.from(fromDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            toD = Date.from(toDate  .atStartOfDay(ZoneId.systemDefault()).toInstant());
        } catch (Exception ex) {
            throw new IllegalArgumentException("Не удалось преобразовать переданные даты.");
        }
        return orderService.getCompletedOrdersCountForTimeSpan(fromD, toD);
    }

    @GetMapping("/get_orders")
    public List<OrderDTO> getCompletedOrdersForTimeSpan(
            @RequestParam("from") String from,
            @RequestParam("to")   String to
    )
    {
        Date fromD = null;
        Date toD   = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDate fromDate = LocalDate.parse(from, formatter);
            LocalDate toDate   = LocalDate.parse(to,   formatter);

            fromD = Date.from(fromDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            toD = Date.from(toDate  .atStartOfDay(ZoneId.systemDefault()).toInstant());
        } catch (Exception ex) {
            throw new IllegalArgumentException("Не удалось преобразовать переданные даты.");
        }
        return orderMapper.toDTOList(orderService.getCompletedOrdersForTimeSpan(fromD, toD));
    }

    @PostMapping
    public void createOrder(@RequestBody OrderDTO orderDTO) throws OrderException, EntityNotFoundException {
        orderService.createOrder(
                orderDTO.getDiscount(),
                orderDTO.getExecutionDate(),
                orderDTO.getClientName(),
                orderDTO.getBookNames()
        );
    }

    @PutMapping("{id}/{status}")
    public void updateOrder(@PathVariable("id") long id, @PathVariable("status") OrderStatus status) throws OrderException, OrderNotFoundException {
        orderService.updateOrder(id, status);
    }

    @GetMapping("/import")
    public void importFromCSV() throws IOException {
        orderService.importOrdersFromCSVFile("task_6/src/main/java/com/yourcompany/task_6_1/Orders.csv");
    }

    @GetMapping("/export")
    public void exportInCSV() throws IOException {
        orderService.exportOrdersIntoCSVFile("task_6/src/main/java/com/yourcompany/task_6_1/Orders.csv");
    }
}
