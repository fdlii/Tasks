package task_3_2;

public class Rose extends Flower {
    private boolean hasThorns;

    public Rose(String color, double price, boolean hasThorns) {
        super("Роза", color, price);
        this.hasThorns = hasThorns;
    }

    public boolean isHasThorns() {
        return hasThorns;
    }

    public void setHasThorns(boolean hasThorns) {
        this.hasThorns = hasThorns;
    }
}
