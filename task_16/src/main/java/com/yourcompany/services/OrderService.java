package com.yourcompany.services;

import com.yourcompany.comparators.OrderExecutionDateComparator;
import com.yourcompany.comparators.OrderFinalPriceComparator;
import com.yourcompany.comparators.OrderStatusComparator;
import com.yourcompany.exceptions.OrderException;
import com.yourcompany.exceptions.OrderNotFoundException;
import com.yourcompany.mappers.BookMapper;
import com.yourcompany.mappers.ClientMapper;
import com.yourcompany.mappers.OrderMapper;
import com.yourcompany.mappers.RequestMapper;
import com.yourcompany.models.*;
import com.yourcompany.repositories.BookRepository;
import com.yourcompany.repositories.ClientRepository;
import com.yourcompany.repositories.OrderRepository;
import com.yourcompany.repositories.RequestRepository;
import com.yourcompany.utils.FileManager;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    Logger logger = LoggerFactory.getLogger(OrderService.class);
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ClientMapper clientMapper;
    @Autowired
    private RequestMapper requestMapper;
    @Autowired
    FileManager fileManager;

    @Transactional
    public List<Order> getOrders() {
        logger.info("Получение всех заказов.");
        List<Order> orders;
        orders = orderMapper.toModelsList(orderRepository.findAll());
        logger.info("Заказы успешно получены.");

        orders.sort(new OrderExecutionDateComparator()
                .thenComparing(new OrderFinalPriceComparator()
                        .thenComparing(new OrderStatusComparator())));
        return orders;
    }

    @Transactional
    public Order getOrderById(long id) throws OrderNotFoundException {
        try {
            logger.info("Получение заказа по id.");
            if (orderRepository.findById(id).isEmpty()) {
                throw new NullPointerException("Не удалось найти запрашиваемый заказ.");
            }
            Order order = orderMapper.toModel(orderRepository.findById(id).get());
            logger.info("Заказ успешно получен.");
            return order;
        }
        catch (NullPointerException ex) {
            logger.error(ex.getMessage());
            throw new OrderNotFoundException(ex.getMessage());
        }
    }

    @Transactional
    public double getEarnedFundsForTimeSpan(Date from, Date to) {
        LocalDateTime fromL = from.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime toL = to.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        logger.info("Получение суммы заработка за период времени.");
        Double funds = orderRepository.getEarnedFundsForTimeSpan(fromL, toL);
        logger.info("Сумма получена успешно.");

        return funds == null ? 0 : funds;
    }

    @Transactional
    public long getCompletedOrdersCountForTimeSpan(Date from, Date to) {
        LocalDateTime fromL = from.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime toL = to.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        logger.info("Получение числа заказов за период времени.");
        Long count = orderRepository.getCompletedOrdersCountForTimeSpan(fromL, toL);
        logger.info("Число заказов получено успешно.");

        return count == null ? 0 : count;
    }

    @Transactional
    public List<Order> getCompletedOrdersForTimeSpan(Date from, Date to) {
        LocalDateTime fromL = from.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime toL = to.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        logger.info("Получение заказов за период времени.");
        List<Order> orders = orderMapper.toModelsList(orderRepository.getCompletedOrdersForTimeSpan(fromL, toL));
        logger.info("Заказы получены успешно.");

        return orders;
    }

    @Transactional
    public Order createOrder(double discount, Date executionDate, String clientName, List<String> bookNames) throws OrderException {
        logger.info("Создание заказа.");
        try {
            if (discount < 0 || discount > 1 || executionDate.before(new Date()) || bookNames.isEmpty()) {
                throw new OrderException("Введены невалидные данные для создания заказа.");
            }

            Client client = clientMapper.toModel(clientRepository.findByName(clientName));

            Order order = new Order(discount, executionDate, client);

            List<Book> books = new ArrayList<>();
            for (String name : bookNames) {
                Book book = bookMapper.toModel(bookRepository.findByName(name));
                if (book.getCountInStock() == 0) {
                    Request request = new Request(book, 1);
                    book.addRequest(request);
                    requestRepository.save(requestMapper.toEntity(request, true));
                }
                books.add(book);
            }
            order.setBooks(books);

            orderRepository.save(orderMapper.toEntity(order, true));
            logger.info("Заказ успешно создан.");
            return order;
        }
        catch (OrderException ex) {
            throw new OrderException(ex.getMessage());
        }
        catch (NullPointerException ex) {
            logger.error(ex.getMessage());
            throw new EntityNotFoundException(ex.getMessage());
        }
    }

    @Transactional
    public Order updateOrder(long orderId, OrderStatus orderStatus) throws OrderException, OrderNotFoundException {
        logger.info("Отмена заказа.");
        try {
            if (orderStatus == null) {
                throw new OrderException("Статус заказа не входит в перечисление.");
            }
            if (orderRepository.findById(orderId).isEmpty()) {
                throw new OrderNotFoundException("Не удалось найти запрашиваемый заказ.");
            }
            Order order = orderMapper.toModel(orderRepository.findById(orderId).get());
            order.changeStatus(orderStatus);
            orderRepository.save(orderMapper.toEntity(order, false));
            logger.info("Заказ успешно изменён.");
            return order;
        }
        catch (OrderException ex) {
            logger.error(ex.getMessage());
            throw new OrderException(ex.getMessage());
        }
        catch (OrderNotFoundException ex) {
            logger.error(ex.getMessage());
            throw new OrderNotFoundException(ex.getMessage());
        }
    }

    @Transactional
    public void importOrdersFromCSVFile(String filename) throws IOException {
        logger.info("Импорт заказов.");
        List<Book> books = bookMapper.toModelsList(bookRepository.findAll());
        List<Client> clients = clientMapper.toModelsList(clientRepository.findAll());
        List<Order> orders = new ArrayList<>();
        fileManager.importOrdersFromCSVFile(filename, orders, clients, books);
        for (Order order : orders) {
            orderRepository.save(orderMapper.toEntity(order, true));
        }
        logger.info("Заказы успешно импортированы.");
    }

    @Transactional
    public void exportOrdersIntoCSVFile(String filename) throws IOException {
        logger.info("Экспорт заказов.");
        fileManager.exportOrdersIntoCSVFile(filename, orderMapper.toModelsList(orderRepository.findAll()));
        logger.info("Заказы успешно экспортированы.");
    }
}
