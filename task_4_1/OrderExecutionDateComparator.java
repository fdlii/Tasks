import java.util.Comparator;

public class OrderExecutionDateComparator implements Comparator<Order> {
    @Override
    public int compare(Order order1, Order order2) {
        return order1.getExecutionDate().compareTo(order2.getExecutionDate());
    }
}
