public class ThirdLineStep implements ILineStep {
    public ThirdLineStep() {
        System.out.println("Запущена 3-я сборочная линия.");
    }

    @Override
    public IProductPart buildProductPart() {
        System.out.println("Выпущена 3-я часть продукта.");
        return new MotherBoard();
    }
}
