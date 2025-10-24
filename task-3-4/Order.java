import java.util.Random;

public class Order {
    private Random random = new Random();
    private int id;
    private Book book;
    private double discount;
    private double finalPrice;
    private OrderStatus orderStatus;

    public Order(Book book, double discount) {
        this.id = random.nextInt(100000); //заглушка
        this.book = book;
        this.discount = discount;
        this.finalPrice = book.getPrice() - book.getPrice() * this.discount;
        this.orderStatus = OrderStatus.NEW;
    }

    public int getId() {
        return id;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void changeStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
