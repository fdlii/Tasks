import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Order {
    private Random random = new Random();
    private int id;
    private Client client;
    private List<Book> books;
    private double discount;
    private double finalPrice;
    private Date executionDate;
    private OrderStatus orderStatus;

    public Order(double discount, Date executionDate, Client client) {
        this.id = random.nextInt(100000);
        this.discount = discount;
        books = new ArrayList<>();
        this.client = client;
        this.executionDate = executionDate;
        this.orderStatus = OrderStatus.NEW;
    }

    public int getId() {
        return id;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public Date getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void addBook(Book book) {
        books.add(book);
        calculateFinalPrice();
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void changeStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    private void calculateFinalPrice() {
        this.finalPrice = 0;
        for (Book book : books) {
            this.finalPrice += book.getPrice() * (1 - discount);
        }
    }
}
