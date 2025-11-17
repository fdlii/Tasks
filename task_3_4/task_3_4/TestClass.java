package task_3_4;

import java.util.Date;

public class TestClass {
    public static void main(String[] args) {
        BookStore.addClient("Никита", 20);
        BookStore.addBook("Маленький принц", "А. Д. Экзюпери", "Книга о приключениях Маленького принца", new Date("12/04/2001"), 449.99, 0);
        BookStore.createOrder(0.1, new Date("23/12/2025"), "Никита","Маленький принц");
        boolean result1 = BookStore.completeOrder(BookStore.getOrders().get(0).getId());
        BookStore.addInStock("Маленький принц", 5);
        boolean result2 = BookStore.completeOrder(BookStore.getOrders().get(0).getId());
        int result3 = BookStore.getBooks().get(0).getCountInStock();

        if (!result1 && result2 && result3 == 4) {
            System.out.println("Логика успешно реализована.");
        }
    }
}
