package com.task_3_2;

//1
public class FlowersHierarchy {
    public static void main(String[] args) {
        Bouquet bouquet = new Bouquet();
        bouquet.addFlower(new Rose("Красный", 100, true));
        bouquet.addFlower(new Chrysanthemum("Зеленый", 50, false));
        bouquet.addFlower(new Tulip("Жёлтый", 80, true));
        bouquet.addFlower(new Rose("Белый", 120, false));

        System.out.println(bouquet.calculatePrice());
    }
}
