package com.yourcompany.services;

import com.yourcompany.entities.BookEntity;
import com.yourcompany.entities.ClientEntity;
import com.yourcompany.entities.OrderEntity;
import com.yourcompany.entities.RequestEntity;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private ClientMapper clientMapper;
    @Mock
    FileManager fileManager;
    @InjectMocks
    OrderService orderService;

    DateTimeFormatter formatter;

    //Data
    BookEntity bookEntity;
    Book book;
    ClientEntity clientEntity;
    Client client;
    Request request;
    RequestEntity requestEntity;
    OrderEntity orderEntity;
    Order order;
    List<Order> orders = new ArrayList<>();
    List<OrderEntity> orderEntities = new ArrayList<>();
    Date from;
    Date to;

    @BeforeEach
    public void setUpData() {
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        book = new Book(1,
                "sdf",
                "S. D. F.",
                "Book",
                Date.from(LocalDate.parse("2005-04-12", formatter).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                5,
                449.99
        );
        bookEntity = new BookEntity(1,
                "sdf",
                "S. D. F.",
                "Book",
                LocalDateTime.of(2005, 4, 12, 0, 0, 0),
                true,
                5,
                449.99);

        clientEntity = new ClientEntity(1, "Nikita", 25);
        client = new Client(1, "Nikita", 25);

        request = new Request(book, 2);
        requestEntity = new RequestEntity(bookEntity, 2, request.isOpen());

        orderEntity = new OrderEntity(
                1,
                clientEntity,
                0,
                449.99,
                LocalDateTime.of(2026, 3, 12, 0, 0, 0),
                OrderStatus.COMPLETED
        );
        orderEntity.setBooks(List.of(bookEntity));
        orderEntities.add(orderEntity);
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
        from = Date.from(LocalDate.parse("2026-02-12", formatter).atStartOfDay(ZoneId.systemDefault()).toInstant());
        to = Date.from(LocalDate.parse("2026-04-12", formatter).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    @Nested
    class GetOrdersTestClass {
        @Test
        public void getOrdersSuccessfullyTest() {
            //When
            when(orderRepository.findAll()).thenReturn(orderEntities);
            when(orderMapper.toModelsList(orderEntities)).thenReturn(orders);

            //Then
            List<Order> returned = orderService.getOrders();
            assertEquals(orders.get(0).getId(), returned.get(0).getId());
            assertEquals(orders.get(0).getClient().getName(), returned.get(0).getClient().getName());
            verify(orderRepository, times(1)).findAll();
        }

        @Test
        public void getOrderByIdSuccessfullyTest() throws OrderNotFoundException {
            //Given
            Long id = 1L;
            Optional<OrderEntity> optionalEntity = Optional.ofNullable(orderEntity);

            //When
            when(orderRepository.findById(id)).thenReturn(optionalEntity);
            when(orderMapper.toModel(optionalEntity.get())).thenReturn(order);

            //Then
            Order returned = orderService.getOrderById(id);
            assertEquals(order.getId(), returned.getId());
            assertEquals(order.getExecutionDate(), returned.getExecutionDate());
            verify(orderRepository, times(2)).findById(any(Long.class));
        }

        @Test
        public void getOrderByIdExceptionTest() {
            //Given
            Long id = 52L;
            Optional<OrderEntity> optionalEntity = Optional.empty();

            //When
            when(orderRepository.findById(id)).thenReturn(optionalEntity);

            //Then
            OrderNotFoundException exception = assertThrows(OrderNotFoundException.class, () -> {
                orderService.getOrderById(id);
            });
            assertEquals("Не удалось найти запрашиваемый заказ.", exception.getMessage());
        }
    }

    @Nested
    class GetOrdersInformationTestClass {
        @Test
        public void getEarnedFundsForTimeSpanSuccessfullyTest() {
            //Given
            Double funds = 449.99;
            LocalDateTime fromL = from.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime toL = to.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            //When
            when(orderRepository.getEarnedFundsForTimeSpan(fromL, toL)).thenReturn(funds);

            //Then
            Double returned = orderService.getEarnedFundsForTimeSpan(from, to);
            assertEquals(funds, returned);
            verify(orderRepository, times(1)).getEarnedFundsForTimeSpan(any(LocalDateTime.class), any(LocalDateTime.class));
        }

        @Test
        public void getCompletedOrdersCountForTimeSpanSuccessfullyTest() {
            //Given
            Long count = 1L;
            LocalDateTime fromL = from.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime toL = to.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            //When
            when(orderRepository.getCompletedOrdersCountForTimeSpan(fromL, toL)).thenReturn(count);

            //Then
            Long returned = orderService.getCompletedOrdersCountForTimeSpan(from, to);
            assertEquals(count, returned);
            verify(orderRepository, times(1)).getCompletedOrdersCountForTimeSpan(any(LocalDateTime.class), any(LocalDateTime.class));
        }

        @Test
        public void getCompletedOrdersForTimeSpanSuccessfullyTest() {
            //Given
            LocalDateTime fromL = from.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime toL = to.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            //When
            when(orderRepository.getCompletedOrdersForTimeSpan(fromL, toL)).thenReturn(orderEntities);
            when(orderMapper.toModelsList(orderEntities)).thenReturn(orders);

            //Then
            List<Order> returned = orderService.getCompletedOrdersForTimeSpan(from, to);
            assertEquals(orders.get(0).getId(), returned.get(0).getId());
            assertEquals(orders.get(0).getClient().getName(), returned.get(0).getClient().getName());
            verify(orderRepository, times(1)).getCompletedOrdersForTimeSpan(any(LocalDateTime.class), any(LocalDateTime.class));
        }
    }

    @Nested
    class CreateOrderTestClass {
        @Test
        public void createOrderSuccessfullyTest() throws OrderException {
            //Given
            String clientName = "Nikita";
            String bookName = "sdf";

            //When
            when(clientRepository.findByName(clientName)).thenReturn(clientEntity);
            when(clientMapper.toModel(clientEntity)).thenReturn(client);
            when(bookRepository.findByName(bookName)).thenReturn(bookEntity);
            when(bookMapper.toModel(bookEntity)).thenReturn(book);
            when(orderMapper.toEntity(any(Order.class), eq(true))).thenReturn(orderEntity);
            when(orderRepository.save(orderEntity)).thenReturn(orderEntity);

            //Then
            Order returned = orderService.createOrder(
                    order.getDiscount(),
                    Date.from(LocalDate.parse("2026-03-27", formatter).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    clientName,
                    List.of(bookName)
            );
            assertEquals(clientName, returned.getClient().getName());
            assertEquals(bookName, returned.getBooks().get(0).getName());
            verify(clientRepository, times(1)).findByName(any(String.class));
            verify(bookRepository, times(1)).findByName(any(String.class));
        }

        @Test
        public void createOrderExceptionTest() {
            //Given
            String clientName = "Nikita";
            String bookName = "sdf";
            double badDiscount = 2D;

            //Then
            OrderException exception = assertThrows(OrderException.class, () -> {
                Order returned = orderService.createOrder(
                        badDiscount,
                        Date.from(LocalDate.parse("2026-03-27", formatter).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        clientName,
                        List.of(bookName)
                );
            });
            assertEquals("Введены невалидные данные для создания заказа.", exception.getMessage());
        }
    }

    @Nested
    class UpdateOrderTestClass {
        @Test
        public void updateOrderSuccessfullyTest() throws OrderNotFoundException, OrderException {
            //Given
            long orderId = 1L;
            Optional<OrderEntity> optional = Optional.ofNullable(orderEntity);
            OrderEntity newOrderEntity = new OrderEntity(
                    1,
                    clientEntity,
                    0,
                    449.99,
                    LocalDateTime.of(2026, 3, 12, 0, 0, 0),
                    OrderStatus.NEW
            );
            newOrderEntity.setBooks(orderEntity.getBooks());

            //When
            when(orderRepository.findById(orderId)).thenReturn(optional);
            when(orderMapper.toModel(optional.get())).thenReturn(order);
            when(orderMapper.toEntity(any(Order.class), eq(false))).thenReturn(newOrderEntity);
            when(orderRepository.save(newOrderEntity)).thenReturn(newOrderEntity);

            //Then
            Order returned = orderService.updateOrder(orderId, OrderStatus.NEW);
            assertEquals(newOrderEntity.getOrderStatus(), returned.getOrderStatus());
            verify(orderRepository, times(1)).save(any(OrderEntity.class));
        }

        @Test
        public void updateOrderExceptionTest() {
            //Given
            long orderId = 1L;

            //Then
            OrderException exception = assertThrows(OrderException.class, () -> {
                Order returned = orderService.updateOrder(orderId, null);
            });
            assertEquals("Статус заказа не входит в перечисление.", exception.getMessage());
        }
    }

    @Nested
    class ImportOrdersTestClass {
        @Test
        public void importOrdersSuccessfullyTest() throws IOException {
            //Given
            String filename = "task_6/src/main/java/com/yourcompany/task_6_1/Orders.csv";
            List<ClientEntity> clientEntities = List.of(clientEntity);
            List<BookEntity> bookEntities = List.of(bookEntity);
            List<Book> books = List.of(book);
            List<Client> clients = List.of(client);

            //When
            when(bookRepository.findAll()).thenReturn(bookEntities);
            when(bookMapper.toModelsList(bookEntities)).thenReturn(books);
            when(clientRepository.findAll()).thenReturn(clientEntities);
            when(clientMapper.toModelsList(clientEntities)).thenReturn(clients);
            doNothing()
                    .when(fileManager)
                    .importOrdersFromCSVFile(filename, orders, clients, books);
            when(orderMapper.toEntity(any(Order.class), eq(true)))
                    .thenReturn(orderEntity);
            when(orderRepository.save(orderEntity))
                    .thenReturn(orderEntity);

            //Then
            assertDoesNotThrow(() -> {
                orderService.importOrdersFromCSVFile(filename);
            });
        }

        @Test
        public void importOrdersExceptionTest() throws IOException {
            //Given
            String filename = "";
            List<ClientEntity> clientEntities = List.of(clientEntity);
            List<BookEntity> bookEntities = List.of(bookEntity);
            List<Book> books = List.of(book);
            List<Client> clients = List.of(client);
            List<Order> orders1 = new ArrayList<>();

            //When
            when(bookRepository.findAll()).thenReturn(bookEntities);
            when(bookMapper.toModelsList(bookEntities)).thenReturn(books);
            when(clientRepository.findAll()).thenReturn(clientEntities);
            when(clientMapper.toModelsList(clientEntities)).thenReturn(clients);
            doThrow(new IOException("Не удалось прочитать файл."))
                    .when(fileManager)
                    .importOrdersFromCSVFile(filename, orders1, clients, books);

            //Then
            IOException exception = assertThrows(IOException.class, () -> {
                orderService.importOrdersFromCSVFile(filename);
            });
            assertEquals("Не удалось прочитать файл.", exception.getMessage());
        }
    }

    @Nested
    class ExportOrdersTestClass {
        @Test
        public void exportOrdersSuccessfullyTest() throws IOException {
            //Given
            String filename = "task_6/src/main/java/com/yourcompany/task_6_1/Orders.csv";

            //When
            doNothing()
                    .when(fileManager)
                    .exportOrdersIntoCSVFile(filename, orders);
            when(orderMapper.toModelsList(any(List.class)))
                    .thenReturn(orders);
            when(orderRepository.findAll())
                    .thenReturn(new ArrayList<OrderEntity>());

            //Then
            assertDoesNotThrow(() -> {
                orderService.exportOrdersIntoCSVFile(filename);
            });
            verify(orderRepository, times(1)).findAll();
        }

        @Test
        public void exportOrdersExceptionTest() throws IOException {
            //Given
            String filename = "";

            //When
            doThrow(new IOException("Не удалось прочитать файл."))
                    .when(fileManager)
                    .exportOrdersIntoCSVFile(filename, orders);
            when(orderMapper.toModelsList(any(List.class)))
                    .thenReturn(orders);
            when(orderRepository.findAll())
                    .thenReturn(new ArrayList<OrderEntity>());

            //Then
            IOException exception = assertThrows(IOException.class, () -> {
                orderService.exportOrdersIntoCSVFile(filename);
            });
            assertEquals("Не удалось прочитать файл.", exception.getMessage());
        }
    }
}