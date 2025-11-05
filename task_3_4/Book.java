import java.util.ArrayList;
import java.util.Date;

public class Book {
    private String name;
    private String author;
    private String description;
    private Date published;
    private boolean inStock;
    private int countInStock;
    private ArrayList<Request> requests = new ArrayList<Request>();
    private double price;

    public Book(String name, String author, String description, Date published, double price) {
        this.name = name;
        this.author = author;
        this.description = description;
        this.published = published;
        this.price = price;
        this.inStock = false;
        this.countInStock = 0;
    }

    public Book(String name, String author, String description, Date published, double price, int countInStock) {
        this.name = name;
        this.author = author;
        this.published = published;
        this.price = price;
        this.inStock = true;
        this.countInStock = countInStock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public int getCountInStock() {
        return countInStock;
    }

    public void setCountInStock(int countInStock) {
        this.countInStock = countInStock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void addRequest(Request request) {
        requests.add(request);
    }

    public void changeRequest(int requestId, boolean status) {
        for (Request request : requests) {
            if (request.getId() == requestId) {
                request.setOpen(status);
                break;
            }
        }
    }

    public ArrayList<Request> getRequests()
    {
        return requests;
    }
}
