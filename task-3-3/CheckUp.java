// 3(сборка ноутбука)
public class CheckUp {
    public static void main(String[] args) {
        IProduct lapTop = new LapTop("Ноутбук №1");
        ILineStep firstStep = new CaseLineStep();
        ILineStep secondStep = new MonitorLineStep();
        ILineStep thirdStep = new MotherBoardLineStep();
        IAssemblyLine assemblyLine = new AssemblyLine(firstStep, secondStep, thirdStep);

        LapTop assembledLaptop = (LapTop) assemblyLine.assembleProduct(lapTop);
        System.out.println("Собранный ноутбук содержит:");
        for (IProductPart productPart : assembledLaptop.parts) {
            System.out.println(productPart.getName());
        }
        System.out.println("Сборка завершена.");
    }
}
