package com.yourcompany.task_3_3;

public class MotherBoard implements IProductPart {
    private final String name = "Материнская плата";

    @Override
    public String getName() {
        return name;
    }
}
