import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Тест сложения ===");
        testSum("99", "1", "100");
        testSum("5", "-3", "2");
        testSum("3", "-5", "-2");

        System.out.println();
        System.out.println("=== Тест вычитания ===");
        testSub("100", "1", "99");

    }

    private static void testSum(String a, String b, String expected) {
        System.out.printf("%s + (%s)\t→ ожидается: %s\t| получено: ", a, b, expected);
        try {
            new LongNumber(a).sum(new LongNumber(b)).print();
        } catch (Exception e) {
            System.out.print("ИСКЛЮЧЕНИЕ: " + e.getMessage());
        }
        System.out.println();
    }

    private static void testSub(String a, String b, String expected) {
        System.out.printf("%s - (%s)\t→ ожидается: %s\t| получено: ", a, b, expected);
        try {
            new LongNumber(a).sub(new LongNumber(b)).print();
        } catch (Exception e) {
            System.out.print("ИСКЛЮЧЕНИЕ: " + e.getMessage());
        }
        System.out.println();
    }
}