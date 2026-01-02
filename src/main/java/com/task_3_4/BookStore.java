package com.task_3_4;

import com.task_11_3.interfaces.IBookDAO;
import com.task_11_3.interfaces.IClientDAO;
import com.task_11_3.interfaces.IOrderDAO;
import com.task_11_3.interfaces.IRequestDAO;
import com.task_4_1.*;
import com.task_6_2.OrderException;
import com.task_8_2.annotations.Inject;
import com.task_8_2.interfaces.IBookStore;
import com.task_8_2.interfaces.IFileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//книжный магазин
public class BookStore implements IBookStore {
    Logger logger = LoggerFactory.getLogger(BookStore.class);
    @Inject
    private IBookDAO bookDAO;
    @Inject
    private IOrderDAO orderDAO;
    @Inject
    private IClientDAO clientDAO;
    @Inject
    private IRequestDAO requestDAO;
    @Inject
    private IFileManager fileManager;

    public BookStore() {}

    @Override
    public void addBook(String name, String author, String description, Date published, double price, int countInStock) throws SQLException {
        logger.info("Добавление книги.");
        try {
            bookDAO.createBook(new Book(name, author, description, published, price, countInStock));
            logger.info("Книга успешно добавлена.");
        } catch (SQLException ex) {
            logger.error("Не удалось добавить книгу в БД.");
            throw new SQLException(ex);
        }
    }

    @Override
    public void deleteBook(String bookName) throws SQLException {
        logger.info("Удаление книги.");
        try {
            bookDAO.deleteBook(bookName);
            logger.info("Книга успешно удалена.");
        } catch (SQLException ex) {
            logger.error("Не удалось удалить книгу из БД.");
            throw new SQLException(ex);
        }
    }

    @Override
    public List<Book> getBooks() throws SQLException {
        logger.info("Получение всех книг.");
        List<Book> books;
        try {
            books = bookDAO.findAll();
            logger.info("Книги успешно получены.");
        } catch (SQLException ex) {
            logger.error("Ошибка при получении книг из БД.");
            throw new SQLException(ex);
        }
        books.sort(new BookNameComparator()
                .thenComparing(new BookPublishedComparator()
                        .thenComparing(new BookPriceComparator()
                                .thenComparing(new BookIsInStockComparator()))));
        return books;
    }

    @Override
    public List<Book> getStaledBooks() throws SQLException {
        logger.info("Получение залежавшихся книг.");
        List<Book> staledBooks;
        try {
            staledBooks = orderDAO.getStaledBooks(Timestamp.valueOf(LocalDateTime.now()));
            logger.info("Книги успешно получены.");
        } catch (SQLException ex) {
            logger.error("Ошибка при получении книг из БД.");
            throw new SQLException(ex);
        }
        staledBooks.sort(new BookPublishedComparator()
                .thenComparing(new BookPriceComparator()));
        return staledBooks;
    }

    @Override
    public List<Order> getOrders() throws SQLException {
        logger.info("Получение всех заказов.");
        List<Order> orders;
        try {
            orders = orderDAO.findAll();
            logger.info("Заказы успешно получены.");
        }
        catch (SQLException ex) {
            logger.error("Ошибка при получении заказов из БД.");
            throw new SQLException(ex);
        }
        orders.sort(new OrderExecutionDateComparator()
                .thenComparing(new OrderFinalPriceComparator()
                        .thenComparing(new OrderStatusComparator())));
        return orders;
    }

    @Override
    public Order getOrderById(int id) throws SQLException {
        try {
            logger.info("Получение заказа по id.");
            Order order = orderDAO.findById(id);
            logger.info("Заказ успешно получен.");
            return order;
        }
        catch (SQLException ex) {
            logger.error("Не удалось получить заказ по id.");
            throw new SQLException(ex.getMessage());
        }

    }

    @Override
    public List<Client> getClients() throws SQLException {
        logger.info("Получение всех клиентов.");
        List<Client> clients = null;
        try {
            clients = clientDAO.findAll();
            logger.info("Клиенты успешно получены.");
        }
        catch (SQLException ex) {
            logger.error("Ошибка при получении клиентов из БД.");
            throw new SQLException(ex);
        }
        return clients;
    }

    @Override
    public void addClient(String name, int age) throws SQLException {
        logger.info("Добавление клиента.");
        try {
            clientDAO.createClient(new Client(name, age));
            logger.info("Клиент успешно добавлен.");
        } catch (SQLException ex) {
            logger.error("Не удалось добавить клиента в БД.");
            throw new SQLException(ex);
        }
    }

    @Override
    public double getEarnedFundsForTimeSpan(Date from, Date to) throws SQLException {
        logger.info("Получение суммы заработка за период времени.");
        double funds = 0;
        try {
            funds = orderDAO.getEarnedFundsForTimeSpan(from, to);
            logger.info("Сумма получена успешно.");
        } catch (SQLException ex) {
            logger.error("Не удалось получить сумму заработка.");
            throw new SQLException(ex);
        }
        return funds;
    }

    @Override
    public int getCompletedOrdersCountForTimeSpan(Date from, Date to) throws SQLException {
        logger.info("Получение числа заказов за период времени.");
        int count = 0;
        try {
            count = orderDAO.getCompletedOrdersCountForTimeSpan(from, to);
            logger.info("Число заказов получено успешно.");
        } catch (SQLException ex) {
            logger.error("Не удалось получить число заказов.");
            throw new SQLException(ex);
        }
        return count;
    }

    @Override
    public List<Order> getCompletedOrdersForTimeSpan(Date from, Date to) throws SQLException {
        logger.info("Получение заказов за период времени.");
        List<Order> orders;
        try {
            orders = orderDAO.getCompletedOrdersForTimeSpan(from, to);
            logger.info("Заказы получены успешно.");
        } catch (SQLException ex) {
            logger.error("Не удалось получить заказы.");
            throw new SQLException(ex);
        }
        return orders;
    }

    @Override
    public void addInStock(String bookName, int count) throws SQLException {
        logger.info("Добавление книги на склад.");
        boolean isAdded;
        try {
            isAdded = bookDAO.addBookInStock(bookName, count);
            logger.info("Книга успешно добавлена на склад.");
        } catch (SQLException ex) {
            logger.error("Не удалось добавить книгу на склад.");
            throw new SQLException(ex);
        }
        if (!isAdded) {
            throw new RuntimeException();
        }
    }

    @Override
    public void debitFromStock(String bookName) throws SQLException {
        logger.info("Удаление книги со склада.");
        boolean isDebit;
        try {
            isDebit = bookDAO.debitBookFromStock(bookName);
            logger.info("Книга успешно удалена со склада.");
        } catch (SQLException ex) {
            logger.error("Не удалось удалить книгу со склада.");
            throw new SQLException(ex);
        }
        if (!isDebit) {
            throw new RuntimeException();
        }
    }

    @Override
    public Order createOrder(double discount, Date executionDate, String clientName, String... bookNames) throws SQLException {
        logger.info("Создание заказа.");
        try {
            List<Client> clients = clientDAO.findAll();

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

            List<Book> books = bookDAO.findAll();

            for (String bookName : bookNames) {
                for (Book book : books) {
                    if (book.getName().equals(bookName)) {
                        order.addBook(book);
                        if (book.getCountInStock() == 0) {
                            Request request = new Request(book, 1);
                            book.addRequest(request);
                            requestDAO.createRequest(request);
                        }
                    }
                }
            }
            orderDAO.createOrder(order);
            logger.info("Заказ успешно создан.");
            return order;
        } catch (SQLException ex) {
            logger.error("Не удалось создать заказ в БД.");
            throw new SQLException(ex);
        }
    }

    @Override
    public void cancelOrder(int orderId) throws OrderException, SQLException {
        logger.info("Отмена заказа.");
        try {
            for (Order order : orderDAO.findAll()) {
                if (order.getId() == orderId) {
                    order.changeStatus(OrderStatus.CANCELLED);
                    orderDAO.updateOrder(order);
                    logger.info("Заказ успешно отменён.");
                    return;
                }
            }
        } catch (SQLException ex) {
            logger.error("Не удалось обновить заказ в БД.");
            throw new SQLException(ex);
        }
    }

    @Override
    public boolean makeRequest(String bookName, int count) throws SQLException {
        logger.info("Создание запроса.");
        try {
            List<Book> books = bookDAO.findAll();

            for (Book book : books) {
                if (book.getName().equals(bookName)) {
                    Request request = new Request(book, count);
                    book.addRequest(request);
                    requestDAO.createRequest(request);
                    logger.info("Запрос успешно создан.");
                    return true;
                }
            }
        } catch (SQLException ex) {
            logger.error("Не удалось добавить запрос в БД.");
            throw new SQLException(ex);
        }
        return false;
    }

    @Override
    public boolean completeOrder(int orderId) throws OrderException, SQLException {
        logger.info("Выполнение заказа.");
        try {
            for (Order order : orderDAO.findAll()) {
                if (order.getId() == orderId) {
                    order.changeStatus(OrderStatus.COMPLETED);
                    orderDAO.updateOrder(order);
                    logger.info("Заказ успешно выполнен.");
                    return true;
                }
            }
        } catch (SQLException ex) {
            logger.error("Не удалось выполнить заказ в БД.");
            throw new SQLException(ex);
        }
        return false;
    }

    @Override
    public List<Request> getBookRequests(String bookName) throws SQLException {
        logger.info("Получение запросов у книги.");
        try {
            List<Request> requests = bookDAO.getBookRequests(bookName);
            logger.info("Запросы успешно получены.");
            return requests;
        } catch (SQLException ex) {
            logger.error("Не удалось получить запросы книги.");
            throw new SQLException(ex);
        }
    }

    @Override
    public List<Request> getRequests() throws SQLException {
        logger.info("Получение запросов.");
        List<Request> requests;
        try {
            requests = requestDAO.findAll();
            logger.info("Запросы успешно получены");
        } catch (SQLException ex) {
            logger.error("Не удалось получить запросы из БД.");
            throw new SQLException(ex);
        }
        return requests;
    }

    @Override
    public void importBooksFromCSVFile(String filename) throws IOException, SQLException {
        logger.info("Импорт книг.");
        try {
            List<Book> books = new ArrayList<>();
            fileManager.importBooksFromCSVFile(filename, books);
            for (Book book : books) {
                bookDAO.createBook(book);
            }
            logger.info("Книги успешно импортированы.");
        } catch (SQLException ex) {
            logger.error("Не удалось импортировать книги.");
            throw new SQLException(ex);
        }
    }

    @Override
    public void importOrdersFromCSVFile(String filename) throws IOException, SQLException {
        logger.info("Импорт заказов.");
        try {
            List<Book> books = bookDAO.findAll();
            List<Client> clients = clientDAO.findAll();
            List<Order> orders = new ArrayList<>();
            fileManager.importOrdersFromCSVFile(filename, orders, clients, books);
            for (Order order : orders) {
                orderDAO.createOrder(order);
            }
            logger.info("Заказы успешно импортированы.");
        } catch (SQLException ex) {
            logger.error("Не удалось импортировать заказы.");
            throw new SQLException(ex);
        }
    }

    @Override
    public void importClientsFromCSVFile(String filename) throws IOException, SQLException {
        logger.info("Импорт клиентов.");
        List<Client> clients = new ArrayList<>();
        fileManager.importClientsFromCSVFile(filename, clients);
        try {
            for (Client client : clients) {
                clientDAO.createClient(client);
            }
            logger.info("Клиенты успешно импортированы.");
        } catch (SQLException ex) {
            logger.error("Не удалось импортировать клиентов.");
            throw new SQLException(ex);
        }
    }

    @Override
    public void importRequestsFromCSVFile(String filename) throws IOException, SQLException {
        logger.info("Импорт запросов.");
        try {
            List<Book> books = bookDAO.findAll();
            List<Request> requests = new ArrayList<>();
            fileManager.importRequestsFromCSVFile(filename, requests, books);
            for (Request request : requests) {
                requestDAO.createRequest(request);
            }
            logger.info("Запросы успешно импортированы.");
        } catch (SQLException ex) {
            logger.error("Не удалось импортировать запросы.");
            throw new SQLException(ex);
        }
    }

    @Override
    public void exportBooksIntoCSVFile(String filename) throws IOException, SQLException {
        logger.info("Экспорт книг.");
        try {
            fileManager.exportBooksIntoCSVFile(filename, bookDAO.findAll());
            logger.info("Книги успешно экспортированы.");
        } catch (SQLException ex) {
            logger.error("Не удалось экспортировать книги.");
            throw new SQLException(ex);
        }
    }

    @Override
    public void exportOrdersIntoCSVFile(String filename) throws IOException, SQLException {
        logger.info("Экспорт заказов.");
        try {
            fileManager.exportOrdersIntoCSVFile(filename, orderDAO.findAll());
            logger.info("Заказы успешно экспортированы.");
        } catch (SQLException ex) {
            logger.error("Не удалось экспортировать заказы.");
            throw new SQLException(ex);
        }
    }

    @Override
    public void exportClientsIntoCSVFile(String filename) throws IOException, SQLException {
        logger.info("Экспорт клиентов.");
        try {
            fileManager.exportClientsIntoCSVFile(filename, clientDAO.findAll());
            logger.info("Клиенты успешно экспортированы.");
        } catch (SQLException ex) {
            logger.error("Не удалось экспортировать клиентов.");
            throw new SQLException(ex);
        }
    }

    @Override
    public void exportRequestsIntoCSVFile(String filename) throws IOException, SQLException {
        logger.info("Экспорт запросов.");
        try {
            fileManager.exportRequestsIntoCSVFile(filename, requestDAO.findAll());
            logger.info("Запросы успешно экспортированы.");
        } catch (SQLException ex) {
            logger.error("Не удалось экспортировать запросы.");
            throw new SQLException(ex);
        }
    }

//    @Override
//    public void getSerializedObjects() throws IOException {
//        requests = fileManager.deserializeObjects(Request.class, "requests.json");
//        books = fileManager.deserializeObjects(Book.class, "books.json");
//        orders = fileManager.deserializeObjects(Order.class, "orders.json");
//        clients = fileManager.deserializeObjects(Client.class, "clients.json");
//
//
//        for (Book book : books) {
//            List<Request> requests1Copy = new ArrayList<>(book.getRequests());
//            for (Request request1 : requests1Copy) {
//                for (Request request2 : requests) {
//                    if (request1.getId() == request2.getId()) {
//                        book.deleteRequest(request1);
//                        book.addRequest(request2);
//                    }
//                }
//            }
//        }
//
//        for (Order order : orders) {
//            List<Book> books1Copy = new ArrayList<>(order.getBooks());
//            for (Book book1 : books1Copy) {
//                for (Book book2 : books) {
//                    if (book1.getId() == book2.getId()) {
//                        order.deleteBook(book1);
//                        order.addBook(book2);
//                    }
//                }
//            }
//        }
//
//        for (Client client : clients) {
//            List<Order> orders1Copy = new ArrayList<>(client.getOrders());
//            for (Order order1 : orders1Copy) {
//                for (Order order2 : orders) {
//                    if (order1.getId() == order2.getId()) {
//                        client.deleteOrder(order1);
//                        client.addOrder(order2);
//                    }
//                }
//            }
//        }
//
//        for (Book book : books) {
//            for (Request request : book.getRequests()) {
//                request.setBook(book);
//            }
//        }
//
//        for (Client client : clients) {
//            for (Order order : client.getOrders()) {
//                order.setClient(client);
//            }
//        }
//    }
}
