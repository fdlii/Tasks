//2
public class ThreeDigitsNumber {
    public static void main(String[] args) {
        java.util.Random random = new java.util.Random();
        int a = random.nextInt(100, 1000);
        int b = random.nextInt(100, 1000);
        int c = random.nextInt(100, 1000);
        System.out.println("Сгенерированные числа:");
        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
        int sum = a / 100 + b / 100 + c / 100;
        System.out.println("Сумма первых цифр:");
        System.out.println(sum);
    }
}
