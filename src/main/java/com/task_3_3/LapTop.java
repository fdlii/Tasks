package com.task_3_3;

import java.util.ArrayList;
import java.util.List;

public class LapTop implements IProduct {
    public String name;
    public List<IProductPart> parts = new ArrayList<>();

    public LapTop(String name) {
        this.name = name;
    }

    @Override
    public void installFirstPart(IProductPart productPart) {
        parts.add(productPart);
        System.out.println("Корпус установлен в устройство.");
    }

    @Override
    public void installSecondPart(IProductPart productPart) {
        parts.add(productPart);
        System.out.println("Монитор установлен в устройство.");
    }

    @Override
    public void installThirdPart(IProductPart productPart) {
        parts.add(productPart);
        System.out.println("Материская плата установлена в устройство.");
    }
}
