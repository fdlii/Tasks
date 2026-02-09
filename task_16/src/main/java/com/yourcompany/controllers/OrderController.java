package com.yourcompany.controllers;

import com.yourcompany.DTO.OrderDTO;
import com.yourcompany.exceptions.BookNotFoundException;
import com.yourcompany.exceptions.ClientNotFoundException;
import com.yourcompany.exceptions.OrderException;
import com.yourcompany.exceptions.OrderNotFoundException;
import com.yourcompany.mappers.OrderMapper;
import com.yourcompany.models.OrderStatus;
import com.yourcompany.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
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
    public List<OrderDTO> getOrders() throws OrderNotFoundException {
        return orderMapper.toDTOList(orderService.getOrders());
    }

    @GetMapping("/{id}")
    public OrderDTO getOrderById(@PathVariable("id") long id) throws OrderNotFoundException {
        return orderMapper.fromModelToDTO(orderService.getOrderById(id));
    }

    @GetMapping("/get_funds")
    public double getEarnedFundsForTimeSpan(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        Date fromD = Date.from(from.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date toD = Date.from(to.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return orderService.getEarnedFundsForTimeSpan(fromD, toD);
    }

    @GetMapping("/get_orders_count")
    public long getCompletedOrdersCountForTimeSpan(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to)
    {
        Date fromD = Date.from(from.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date toD = Date.from(to.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return orderService.getCompletedOrdersCountForTimeSpan(fromD, toD);
    }

    @GetMapping("/get_orders")
    public List<OrderDTO> getCompletedOrdersForTimeSpan(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) throws OrderNotFoundException {
        Date fromD = Date.from(from.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date toD = Date.from(to.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return orderMapper.toDTOList(orderService.getCompletedOrdersForTimeSpan(fromD, toD));
    }

    @PostMapping
    public void createOrder(@RequestBody OrderDTO orderDTO) throws ClientNotFoundException, BookNotFoundException {
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
    public void importFromCSV() throws IOException, ClientNotFoundException, BookNotFoundException {
        orderService.importOrdersFromCSVFile("task_6/src/main/java/com/yourcompany/task_6_1/Orders.csv");
    }

    @GetMapping("/export")
    public void exportInCSV() throws IOException, OrderNotFoundException {
        orderService.exportOrdersIntoCSVFile("task_6/src/main/java/com/yourcompany/task_6_1/Orders.csv");
    }
}
