package com.task_3_3;

public class MonitorLineStep implements ILineStep {
    public MonitorLineStep() {
        System.out.println("Запущена сборочная линия мониторов.");
    }

    @Override
    public IProductPart buildProductPart() {
        System.out.println("Выпущен монитор.");
        return new Monitor();
    }
}
