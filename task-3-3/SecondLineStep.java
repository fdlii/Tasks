public class SecondLineStep implements ILineStep {
    public SecondLineStep() {
        System.out.println("Запущена 2-я сборочная линия.");
    }

    @Override
    public IProductPart buildProductPart() {
        System.out.println("Выпущена 2-я часть продукта.");
        return new Monitor();
    }
}
