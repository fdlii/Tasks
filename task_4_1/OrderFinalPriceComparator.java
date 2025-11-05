import java.util.Comparator;

public class OrderFinalPriceComparator implements Comparator<Order> {
    @Override
    public int compare(Order order1, Order order2) {
        double diff = order1.getFinalPrice() - order2.getFinalPrice();

        if (diff < 0) {
            return -1;
        } else if (diff > 0){
            return 1;
        } else {
            return 0;
        }
    }
}
