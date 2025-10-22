public class Tulip extends Flower {
    private boolean isOpen;

    public Tulip(String color, double price, boolean isOpen) {
        super("Тюльпан", color, price);
        this.isOpen = isOpen;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
