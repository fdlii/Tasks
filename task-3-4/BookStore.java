import java.util.ArrayList;

//книжный магазин
public class BookStore {
    private ArrayList<Book> books = new ArrayList<Book>();
    private ArrayList<Order> orders = new ArrayList<Order>();

    public void addBook(Book book) {
        books.add(book);
    }

    public void deleteBook(String bookName) {
        for (Book b : books) {
            if (b.getName().equals(bookName)) {
                books.remove(b);
                break;
            }
        }
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void addInStock(String bookName, int count) {
        for (Book b : books) {
            if (b.getName().equals(bookName)) {
                int sum = b.getCountInStock() + count;
                b.setCountInStock(sum);
                b.setInStock(true);

                for (Request r : b.getRequests()) {
                    if (r.getCount() <= b.getCountInStock() && r.isOpen()) {
                        r.setOpen(false);
                        b.setCountInStock(b.getCountInStock() - r.getCount());
                    }
                }

                if (b.getCountInStock() == 0) {
                    b.setInStock(false);
                }

                break;
            }
        }
    }

    public void debitFromStock(String bookName) {
        for (Book b : books) {
            if (b.getName().equals(bookName)) {
                b.setCountInStock(0);
                b.setInStock(false);
                break;
            }
        }
    }

    public Order createOrder(String bookName, double discount) {
        for (Book b : books) {
            if (b.getName().equals(bookName)) {
                Order order = new Order(b, discount);
                orders.add(order);
                if (b.getCountInStock() == 0) {
                    b.AddRequest(new Request(b, 1));
                }
                return order;
            }
        }
        return null;
    }

    public void cancelOrder(int orderId) {
        for (Order order : orders) {
            if (order.getId() == orderId) {
                order.ChangeStatus(OrderStatus.CANCELLED);
                break;
            }
        }
    }

    public boolean makeRequest(String bookName, int count) {
        for (Book b : books) {
            if (b.getName().equals(bookName)) {
                if (b.getCountInStock() != 0) {
                    return false;
                }
                Request request = new Request(b, count);
                b.AddRequest(request);
                return true;
            }
        }
        return false;
    }

    public boolean completeOrder(int orderId) {
        for (Order order : orders) {
            if (order.getId() == orderId) {
                for (Request request : order.getBook().getRequests()) {
                    if (request.isOpen()) {
                        return false;
                    }
                }
                order.ChangeStatus(OrderStatus.COMPLETED);
                return true;
            }
        }
        return false;
    }
}
