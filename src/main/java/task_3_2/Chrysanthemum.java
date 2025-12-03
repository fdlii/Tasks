package task_3_2;

public class Chrysanthemum extends Flower {
    private boolean isSpray;

    public Chrysanthemum(String color, double price, boolean isSpray) {
        super("Хризантема", color, price);
        this.isSpray = isSpray;
    }

    public boolean isSpray() {
        return isSpray;
    }

    public void setSpray(boolean spray) {
        isSpray = spray;
    }
}
