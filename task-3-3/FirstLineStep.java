public class FirstLineStep implements ILineStep {
    public FirstLineStep() {
        System.out.println("Запущена 1-я сборочная линия.");
    }

    @Override
    public IProductPart buildProductPart() {
        System.out.println("Выпущена 1-я часть продукта.");
        return new Corpus();
    }
}
