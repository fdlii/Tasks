package com.task_3_3;

public class MotherBoardLineStep implements ILineStep {
    public MotherBoardLineStep() {
        System.out.println("Запущена сборочная линия материнских плат.");
    }

    @Override
    public IProductPart buildProductPart() {
        System.out.println("Выпущена материнская плата.");
        return new MotherBoard();
    }
}
