import java.util.ArrayList;

public class Bouquet {
    private ArrayList<Flower> flowers;

    public Bouquet() {
        flowers = new ArrayList<Flower>();
    }

    public Bouquet(ArrayList<Flower> flowers) {
        this.flowers = flowers;
    }

    public void addFlower(Flower flower) {
        flowers.add(flower);
    }

    public double calculatePrice() {
        double sum = 0;
        for(Flower f : flowers){
            sum += f.getPrice();
        }
        return sum;
    }
}
