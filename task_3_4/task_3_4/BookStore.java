package task_3_4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import task_4_1.*;

//книжный магазин
public class BookStore {
    private static List<Book> books = new ArrayList<>();
    private static List<Order> orders = new ArrayList<>();
    private static List<Client> clients = new ArrayList<>();
    private static List<Request> requests = new ArrayList<>();

    public static void addBook(String name, String author, String description, Date published, double price, int countInStock) {
        books.add(new Book(name, author, description, published, price, countInStock));
    }

    public static void deleteBook(String bookName) {
        books.stream()
                .filter(book -> book.getName().equals(bookName))
                .forEach(book -> {
                    for (Request r : book.getRequests()) {
                        requests.remove(r);
                    }
                    books.remove(book);
                });
    }

    public static List<Book> getBooks() {
        books.sort(new BookNameComparator()
                .thenComparing(new BookPublishedComparator()
                        .thenComparing(new BookPriceComparator()
                                .thenComparing(new BookIsInStockComparator()))));
        return books;
    }

    public static List<Book> getStaledBooks() {
        List<Book> notStaledBooks = new ArrayList<>();
        for (Order order : orders) {
            if (ChronoUnit.MONTHS.between((new Date()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    order.getExecutionDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()) < 6)
            {
                notStaledBooks.addAll(order.getBooks());
            }
        }

        List<Book> staledBooks = new ArrayList<>();
        for (Book book : books) {
            boolean flag = false;
            for (Book staledBook : staledBooks) {
                if (book.equals(staledBook)) {
                    flag = true;
                }
            }
            if (!flag) {
                staledBooks.add(book);
            }
        }

        staledBooks.sort(new BookPublishedComparator()
                .thenComparing(new BookPriceComparator()));
        return staledBooks;
    }

    public static List<Order> getOrders() {
        orders.sort(new OrderExecutionDateComparator()
                .thenComparing(new OrderFinalPriceComparator()
                        .thenComparing(new OrderStatusComparator())));
        return orders;
    }

    public static Order getOrderById(int id) {
        return orders.stream()
                .filter(order1 -> order1.getId() == id)
                .findFirst()
                .get();
    }

    public static List<Client> getClients() {
        return clients;
    }

    public static void addClient(String name, int age) {
        clients.add(new Client(name, age));
    }

    public static double getEarnedFundsForTimeSpan(Date from, Date to) {
        double sum = 0;
        for (Order order : orders) {
            if
            (
                    order.getExecutionDate().compareTo(from) > 0 &&
                    to.compareTo(order.getExecutionDate()) > 0 &&
                    order.getOrderStatus() == OrderStatus.COMPLETED
            )
            {
                sum += order.getFinalPrice();
            }
        }
        return sum;
    }

    public static int getCompletedOrdersCountForTimeSpan(Date from, Date to) {
        int count = 0;
        for (Order order : orders) {
            if
            (
                    order.getExecutionDate().compareTo(from) > 0 &&
                    to.compareTo(order.getExecutionDate()) > 0 &&
                    order.getOrderStatus() == OrderStatus.COMPLETED
            )
            {
                count += 1;
            }
        }
        return count;
    }

    public static List<Order> getCompletedOrdersForTimeSpan(Date from, Date to) {
        List<Order> validOrders = new ArrayList<>();
        for (Order order : orders) {
            if
            (
                    order.getExecutionDate().compareTo(from) > 0 &&
                    to.compareTo(order.getExecutionDate()) > 0 &&
                    order.getOrderStatus() == OrderStatus.COMPLETED)
            {
                validOrders.add(order);
            }
        }
        validOrders.sort(new OrderExecutionDateComparator()
                .thenComparing(new OrderFinalPriceComparator()));
        return validOrders;
    }

    public static void addInStock(String bookName, int count) {
        for (Book book : books) {
            if (book.getName().equals(bookName)) {
                int sum = book.getCountInStock() + count;
                book.setCountInStock(sum);
                book.setInStock(true);

                for (Request r : book.getRequests()) {
                    if (r.getCount() <= book.getCountInStock() && r.isOpen()) {
                        r.setOpen(false);
                        book.setCountInStock(book.getCountInStock() - r.getCount());
                    }
                }

                if (book.getCountInStock() == 0) {
                    book.setInStock(false);
                }

                break;
            }
        }
    }

    public static void debitFromStock(String bookName) {
        books.stream()
                .filter(book -> book.getName().equals(bookName))
                .forEach(book -> {
                    book.setCountInStock(0);
                    book.setInStock(false);
                });
    }

    public static Order createOrder(double discount, Date executionDate, String clientName, String... bookNames) {
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
        for (String bookName : bookNames) {
            for (Book book : books) {
                if (book.getName().equals(bookName)) {
                    order.addBook(book);
                    if (book.getCountInStock() == 0) {
                        Request request = new Request(book, 1);
                        book.addRequest(request);
                        requests.add(request);
                    }
                }
            }
        }
        orders.add(order);
        return order;
    }

    public static void cancelOrder(int orderId) {
        orders.stream()
                .filter(order -> order.getId() == orderId)
                .forEach(order -> order.changeStatus(OrderStatus.CANCELLED));
    }

    public static boolean makeRequest(String bookName, int count) {
        for (Book book : books) {
            if (book.getName().equals(bookName)) {
                if (book.getCountInStock() != 0) {
                    return false;
                }
                Request request = new Request(book, count);
                book.addRequest(request);
                requests.add(request);
                return true;
            }
        }
        return false;
    }

    public static boolean completeOrder(int orderId) {
        for (Order order : orders) {
            if (order.getId() == orderId) {
                for (Book book : order.getBooks()) {
                    for (Request request : book.getRequests()) {
                        if (request.isOpen()) {
                            return false;
                        }
                    }
                }
                order.changeStatus(OrderStatus.COMPLETED);
                return true;
            }
        }
        return false;
    }

    public static List<Request> getBookRequests(String bookName) {
        return books.stream()
                .filter(book -> book.getName().equals(bookName))
                .findFirst()
                .get()
                .getRequests();
    }

    public static void importBooksFromCSVFile(String filename) throws IOException {
        List<String[]> records = parseCSV(filename);

        for (String[] record : records) {
            boolean bookFound = false;

            for (Book b : books) {
                if (b.getName().equals(record[0])) {
                    b.setAuthor(record[1]);
                    b.setDescription(record[2]);
                    b.setPublished(new Date(record[3]));
                    b.setPrice(Double.parseDouble(record[4]));
                    b.setCountInStock(Integer.parseInt(record[5]));
                    bookFound = true;
                    break;
                }
            }

            if (!bookFound) {
                Book book = new Book(record[0],
                        record[1],
                        record[2],
                        new Date(record[3]),
                        Double.parseDouble(record[4]),
                        Integer.parseInt(record[5]));

                if (record.length > 6) {
                    for (int i = 6; i < record.length; i++) {
                        for (Request r : requests) {
                            if (r.getId() == Integer.parseInt(record[i])) {
                                book.addRequest(r);
                                break;
                            }
                        }
                    }
                }

                books.add(book);
            }
        }
    }

    public static void importOrdersFromCSVFile(String filename) throws IOException {
        List<String[]> records = parseCSV(filename);

        for (String[] record : records) {
            boolean orderFound = false;

            boolean clientFound = false;
            Client foundedClient = null;
            for (Client c : clients) {
                if (c.getName().equals(record[3])) {
                    clientFound = true;
                    foundedClient = c;
                    break;
                }
            }
            if (!clientFound) {
                throw new RuntimeException();
            }

            for (Order o : orders) {
                if (o.getId() == Integer.parseInt(record[0])) {
                    o.setDiscount(Double.parseDouble(record[1]));
                    o.setExecutionDate(new Date(record[2]));
                    o.setClient(foundedClient);
                    orderFound = true;
                    break;
                }
            }

            if (!orderFound) {
                Order order = new Order(Double.parseDouble(record[1]),
                        new Date(record[2]),
                        foundedClient);

                if (record.length > 4) {
                    for (int i = 4; i < record.length; i++) {
                        for (Book b : books) {
                            if (b.getId() == Integer.parseInt(record[i])) {
                                order.addBook(b);
                                break;
                            }
                        }
                    }
                }

                foundedClient.addOrder(order);
                orders.add(order);
            }
        }
    }

    public static void importClientsFromCSVFile(String filename) throws IOException {
        List<String[]> records = parseCSV(filename);

        for (String[] record : records) {
            boolean clientFound = false;

            for (Client c : clients) {
                if (c.getName().equals(record[0])) {
                    c.setAge(Integer.parseInt(record[1]));
                    clientFound = true;
                    break;
                }
            }

            if (!clientFound) {
                Client client = new Client(record[0], Integer.parseInt(record[1]));

                if (record.length > 2) {
                    for (int i = 2; i < record.length; i++) {
                        for (Order o : orders) {
                            if (o.getId() == Integer.parseInt(record[i])) {
                                client.addOrder(o);
                                break;
                            }
                        }
                    }
                }

                clients.add(client);
            }
        }
    }

    public static void ExportBooksIntoCSVFile(String fileName) throws IOException {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (Book book : books) {
                writer.append(book.getName()).append(",");
                writer.append(book.getAuthor()).append(",");
                writer.append(book.getDescription()).append(",");
                writer.append(book.getPublished().toString()).append(",");
                writer.append(String.valueOf(book.getPrice())).append(",");
                writer.append(String.valueOf(book.getCountInStock())).append(",");

                List<Request> requests = book.getRequests();
                if (requests != null && !requests.isEmpty()) {
                    for (int i = 0; i < requests.size(); i++) {
                        if (i > 0) writer.append(",");
                        writer.append(String.valueOf(requests.get(i).getId()));
                    }
                }
                writer.append("\n");
            }
        }
    }

    public static void ExportOrdersIntoCSVFile(String fileName) throws IOException {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (Order order : orders) {
                writer.append(String.valueOf(order.getId())).append(",");
                writer.append(String.valueOf(order.getDiscount())).append(",");
                writer.append(String.valueOf(order.getExecutionDate())).append(",");
                writer.append(String.valueOf(order.getClient().getName())).append(",");

                List<Book> books = order.getBooks();
                if (books != null && !books.isEmpty()) {
                    for (int i = 0; i < books.size(); i++) {
                        if (i > 0) writer.append(",");
                        writer.append(String.valueOf(books.get(i).getId()));
                    }
                }
                writer.append("\n");
            }
        }
    }

    public static void ExportClientsIntoCSVFile(String fileName) throws IOException{
        try (FileWriter writer = new FileWriter(fileName)) {
            for (Client client : clients) {
                writer.append(client.getName()).append(",");
                writer.append(String.valueOf(client.getAge())).append(",");

                List<Order> orders = client.getOrders();
                if (orders != null && !orders.isEmpty()) {
                    for (int i = 0; i < orders.size(); i++) {
                        if (i > 0) writer.append(",");
                        writer.append(String.valueOf(orders.get(i).getId()));
                    }
                }
                writer.append("\n");
            }
        }
    }

    private static List<String[]> parseCSV(String fileName) throws IOException {
        List<String[]> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                records.add(values);
            }
        }
        return records;
    }
}
