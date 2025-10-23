import java.util.ArrayList;

//книжный магазин
public class BookStore {
    private ArrayList<Book> books = new ArrayList<Book>();
    private ArrayList<Order> orders = new ArrayList<Order>();

    public void addBook(Book book) {
        books.add(book);
    }

    public void deleteBook(String bookName) {
        for (Book book : books) {
            if (book.getName().equals(bookName)) {
                books.remove(book);
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
        for (Book book : books) {
            if (book.getName().equals(bookName)) {
                book.setCountInStock(0);
                book.setInStock(false);
                break;
            }
        }
    }

    public Order createOrder(String bookName, double discount) {
        for (Book book : books) {
            if (book.getName().equals(bookName)) {
                Order order = new Order(book, discount);
                orders.add(order);
                if (book.getCountInStock() == 0) {
                    book.addRequest(new Request(book, 1));
                }
                return order;
            }
        }
        return null;
    }

    public void cancelOrder(int orderId) {
        for (Order order : orders) {
            if (order.getId() == orderId) {
                order.changeStatus(OrderStatus.CANCELLED);
                break;
            }
        }
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
                for (Request request : order.getBook().getRequests()) {
                    if (request.isOpen()) {
                        return false;
                    }
                }
                order.changeStatus(OrderStatus.COMPLETED);
                return true;
            }
        }
        return false;
    }
}
