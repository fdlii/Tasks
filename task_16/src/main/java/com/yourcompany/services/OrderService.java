package com.yourcompany.services;

import com.yourcompany.comparators.OrderExecutionDateComparator;
import com.yourcompany.comparators.OrderFinalPriceComparator;
import com.yourcompany.comparators.OrderStatusComparator;
import com.yourcompany.exceptions.OrderException;
import com.yourcompany.mappers.BookMapper;
import com.yourcompany.mappers.ClientMapper;
import com.yourcompany.mappers.OrderMapper;
import com.yourcompany.mappers.RequestMapper;
import com.yourcompany.models.*;
import com.yourcompany.task_13.DAOs.BookDAO;
import com.yourcompany.task_13.DAOs.ClientDAO;
import com.yourcompany.task_13.DAOs.OrderDAO;
import com.yourcompany.task_13.DAOs.RequestDAO;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderService {
    Logger logger = LoggerFactory.getLogger(OrderService.class);
    @Autowired
    private BookDAO bookDAO;
    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private ClientDAO clientDAO;
    @Autowired
    private RequestDAO requestDAO;
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
    public List<Order> getOrders() throws HibernateException {
        logger.info("Получение всех заказов.");
        List<Order> orders;
        try {
            orders = orderMapper.toModelsList(orderDAO.findAll());
            logger.info("Заказы успешно получены.");
        }
        catch (HibernateException ex) {
            logger.error("Ошибка при получении заказов из БД.");
            throw new HibernateException(ex);
        }
        orders.sort(new OrderExecutionDateComparator()
                .thenComparing(new OrderFinalPriceComparator()
                        .thenComparing(new OrderStatusComparator())));
        return orders;
    }

    @Transactional
    public Order getOrderById(long id) throws HibernateException {
        try {
            logger.info("Получение заказа по id.");
            Order order = orderMapper.toModel(orderDAO.findById(id));
            logger.info("Заказ успешно получен.");
            return order;
        }
        catch (HibernateException ex) {
            logger.error("Не удалось получить заказ по id.");
            throw new HibernateException(ex.getMessage());
        }
    }

    @Transactional
    public double getEarnedFundsForTimeSpan(Date from, Date to) throws HibernateException {
        logger.info("Получение суммы заработка за период времени.");
        double funds = 0;
        try {
            funds = orderDAO.getEarnedFundsForTimeSpan(from, to);
            logger.info("Сумма получена успешно.");
        } catch (HibernateException ex) {
            logger.error("Не удалось получить сумму заработка.");
            throw new HibernateException(ex);
        }
        return funds;
    }

    @Transactional
    public long getCompletedOrdersCountForTimeSpan(Date from, Date to) throws HibernateException {
        logger.info("Получение числа заказов за период времени.");
        long count = 0;
        try {
            count = orderDAO.getCompletedOrdersCountForTimeSpan(from, to);
            logger.info("Число заказов получено успешно.");
        } catch (HibernateException ex) {
            logger.error("Не удалось получить число заказов.");
            throw new HibernateException(ex);
        }
        return count;
    }

    @Transactional
    public List<Order> getCompletedOrdersForTimeSpan(Date from, Date to) throws HibernateException {
        logger.info("Получение заказов за период времени.");
        List<Order> orders;
        try {
            orders = orderMapper.toModelsList(orderDAO.getCompletedOrdersForTimeSpan(from, to));
            logger.info("Заказы получены успешно.");
        } catch (HibernateException ex) {
            logger.error("Не удалось получить заказы.");
            throw new HibernateException(ex);
        }
        return orders;
    }

    @Transactional
    public Order createOrder(double discount, Date executionDate, String clientName, String... bookNames) throws HibernateException {
        logger.info("Создание заказа.");
        try {
            List<Client> clients = clientMapper.toModelsList(clientDAO.findAll());

            Order order = null;
            boolean flag = false;
            for (Client client : clients) {
                if (clientName.equals(client.getName())) {
                    order = new Order(discount, executionDate, client);
                    flag = true;
                    break;
                }
            }
            if (!flag)
                return null;

            List<Book> books = bookMapper.toModelsList(bookDAO.findAll());

            for (String bookName : bookNames) {
                for (Book book : books) {
                    if (book.getName().equals(bookName)) {
                        order.addBook(book);
                        if (book.getCountInStock() == 0) {
                            Request request = new Request(book, 1);
                            book.addRequest(request);
                            requestDAO.save(requestMapper.toEntity(request, true));
                        }
                    }
                }
            }
            orderDAO.save(orderMapper.toEntity(order, true));
            logger.info("Заказ успешно создан.");
            return order;
        } catch (HibernateException ex) {
            logger.error("Не удалось создать заказ в БД.");
            throw new HibernateException(ex);
        }
    }

    @Transactional
    public void cancelOrder(long orderId) throws OrderException, HibernateException {
        logger.info("Отмена заказа.");
        try {
            for (Order order : orderMapper.toModelsList(orderDAO.findAll())) {
                if (order.getId() == orderId) {
                    order.changeStatus(OrderStatus.CANCELLED);
                    orderDAO.update(orderMapper.toEntity(order, false));
                    logger.info("Заказ успешно отменён.");
                    return;
                }
            }
        } catch (HibernateException ex) {
            logger.error("Не удалось обновить заказ в БД.");
            throw new HibernateException(ex);
        }
    }

    @Transactional
    public boolean completeOrder(long orderId) throws OrderException, HibernateException {
        logger.info("Выполнение заказа.");
        try {
            for (Order order : orderMapper.toModelsList(orderDAO.findAll())) {
                if (order.getId() == orderId) {
                    order.changeStatus(OrderStatus.COMPLETED);
                    orderDAO.update(orderMapper.toEntity(order, false));
                    logger.info("Заказ успешно выполнен.");
                    return true;
                }
            }
        } catch (HibernateException ex) {
            logger.error("Не удалось выполнить заказ в БД.");
            throw new HibernateException(ex);
        }
        return false;
    }

    @Transactional
    public void importOrdersFromCSVFile(String filename) throws IOException, HibernateException {
        logger.info("Импорт заказов.");
        try {
            List<Book> books = bookMapper.toModelsList(bookDAO.findAll());
            List<Client> clients = clientMapper.toModelsList(clientDAO.findAll());
            List<Order> orders = new ArrayList<>();
            fileManager.importOrdersFromCSVFile(filename, orders, clients, books);
            for (Order order : orders) {
                orderDAO.save(orderMapper.toEntity(order, true));
            }
            logger.info("Заказы успешно импортированы.");
        } catch (HibernateException ex) {
            logger.error("Не удалось импортировать заказы.");
            throw new HibernateException(ex);
        }
    }

    @Transactional
    public void exportOrdersIntoCSVFile(String filename) throws IOException, HibernateException {
        logger.info("Экспорт заказов.");
        try {
            fileManager.exportOrdersIntoCSVFile(filename, orderMapper.toModelsList(orderDAO.findAll()));
            logger.info("Заказы успешно экспортированы.");
        } catch (HibernateException ex) {
            logger.error("Не удалось экспортировать заказы.");
            throw new HibernateException(ex);
        }
    }
}
