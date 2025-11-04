import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//книжный магазин
public class BookStore {
    private List<Book> books = new ArrayList<>();
    private List<Order> orders = new ArrayList<>();
    private List<Client> clients = new ArrayList<>();

    public void addBook(Book book) {
        books.add(book);
    }

    public void deleteBook(String bookName) {
        books.stream()
                .filter(book -> book.getName().equals(bookName))
                .forEach(book -> books.remove(book));
    }

    public List<Book> getBooks() {
        books.sort(new BookNameComparator()
                .thenComparing(new BookPublishedComparator()
                        .thenComparing(new BookPriceComparator()
                                .thenComparing(new BookIsInStockComparator()))));
        return books;
    }

    public List<Book> getStaledBooks() {
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

    public List<Order> getOrders() {
        orders.sort(new OrderExecutionDateComparator()
                .thenComparing(new OrderFinalPriceComparator()
                        .thenComparing(new OrderStatusComparator())));
        return orders;
    }

    public Order getOrderById(int id) {
        return orders.stream()
                .filter(order1 -> order1.getId() == id)
                .findFirst()
                .get();
    }

    public List<Client> getClients() {
        return clients;
    }

    public void addClient(String name, int age) {
        clients.add(new Client(name, age));
    }

    public double getEarnedFundsForTimeSpan(Date from, Date to) {
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

    public int getCompletedOrdersCountForTimeSpan(Date from, Date to) {
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

    public List<Order> getCompletedOrdersForTimeSpan(Date from, Date to) {
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

    public void addInStock(String bookName, int count) {
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

    public void debitFromStock(String bookName) {
        books.stream()
                .filter(book -> book.getName().equals(bookName))
                .forEach(book -> {
                    book.setCountInStock(0);
                    book.setInStock(false);
                });
    }

    public Order createOrder(double discount, Date executionDate, String clientName, String... bookNames) {
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
                        book.addRequest(new Request(book, 1));
                    }
                }
            }
        }
        orders.add(order);
        return order;
    }

    public void cancelOrder(int orderId) {
        orders.stream()
                .filter(order -> order.getId() == orderId)
                .forEach(order -> order.changeStatus(OrderStatus.CANCELLED));
    }

    public boolean makeRequest(String bookName, int count) {
        for (Book book : books) {
            if (book.getName().equals(bookName)) {
                if (book.getCountInStock() != 0) {
                    return false;
                }
                Request request = new Request(book, count);
                book.addRequest(request);
                return true;
            }
        }
        return false;
    }

    public boolean completeOrder(int orderId) {
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

    public List<Request> getBookRequests(String bookName) {
        return books.stream()
                .filter(book -> book.getName().equals(bookName))
                .findFirst()
                .get()
                .getRequests();
    }
}
