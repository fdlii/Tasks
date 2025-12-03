package task_3_4;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Client {
    private static int counter = 1;

    private int id;
    private String name;
    private int age;
    private List<Order> orders;

    public Client(String name, int age) {
        this.id = counter++; //заглушка
        this.name = name;
        this.age = age;
        this.orders = new ArrayList<>();
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
