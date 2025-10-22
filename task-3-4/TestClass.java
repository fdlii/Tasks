import java.util.Date;

public class TestClass {
    public static void main(String[] args) {
        BookStore bookStore = new BookStore();
        bookStore.addBook(new Book("Маленький принц", "А. Д. Экзюпери", new Date("12/04/2001"), 449.99));
        bookStore.createOrder("Маленький принц", 0.1);
        boolean result1 = bookStore.completeOrder(bookStore.getOrders().get(0).getId());
        bookStore.addInStock("Маленький принц", 5);
        boolean result2 = bookStore.completeOrder(bookStore.getOrders().get(0).getId());
        int result3 = bookStore.getBooks().get(0).getCountInStock();

        if (!result1 && result2 && result3 == 4) {
            System.out.println("Логика успешно реализована.");
        }
    }
}
