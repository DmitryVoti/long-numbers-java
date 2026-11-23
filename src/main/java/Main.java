import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 1. Ввод и вывод


            System.out.print("Введите первое длинное число: ");
            LongNumber a = new LongNumber(scanner.nextLine().trim());

            System.out.print("Введите второе длинное число: ");
            LongNumber b = new LongNumber(scanner.nextLine().trim());

            System.out.print("Число A: ");
            a.print();
            System.out.println();

            System.out.print("Число B: ");
            b.print();
            System.out.println("\n");

            // 2. Сравнение
            byte cmp = a.isItMoreThan(b);
            String cmpSign = cmp > 0 ? ">" : (cmp < 0 ? "<" : "=");
            System.out.println("Сравнение: A " + cmpSign + " B (код: " + cmp + ")");

            // 3. Сложение
            System.out.print("A + B = ");
            a.sum(b).print();
            System.out.println();

            // 4. Вычитание
            System.out.print("A - B = ");
            a.sub(b).print();
            System.out.println();

            // 5. Умножение
            System.out.print("A * B = ");
            a.multiply(b).print();
            System.out.println();

            // 6. Деление с остатком (целая часть и остаток)
            try {
                divideResult div = a.division(b);
                System.out.print("A / B (целая часть) = ");
                div.result.print();
                System.out.print(", остаток = ");
                div.remainder.print();
                System.out.println();
            } catch (IllegalArgumentException | ArithmeticException e) {
                System.out.println("Деление для A и B недоступно: " + e.getMessage());
            }

            // 7. Возведение в степень (бинарный алгоритм)
            System.out.print("Введите целую неотрицательную степень для A: ");
            int power = scanner.nextInt();
            try {
                System.out.print("A ^ " + power + " = ");
                a.binaryPower(power).print();
                System.out.println();
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка возведения в степень: " + e.getMessage());
            }

        scanner.close();
    }
}