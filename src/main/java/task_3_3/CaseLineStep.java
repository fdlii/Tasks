package task_3_3;

public class CaseLineStep implements ILineStep {
    public CaseLineStep() {
        System.out.println("Запущена сборочная линия корпусов.");
    }

    @Override
    public IProductPart buildProductPart() {
        System.out.println("Выпущен корпус.");
        return new Case();
    }
}
