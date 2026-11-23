import java.util.Arrays;

public class LongNumber {
    private final byte[] numbers;
    private final boolean isItPositive;

    public LongNumber(byte[] numbers, boolean isItPositive) {
        if (numbers == null || numbers.length == 0) {
            throw new IllegalArgumentException("Пустой массив цифр");
        }
        byte[] copy = removeExtraZeros(numbers);
        this.numbers = copy;
        this.isItPositive = copy.length == 1 && copy[0] == 0 || isItPositive;
    }

    public LongNumber(LongNumber ln) {
        this(ln.numbers, ln.isItPositive);
    }

    public LongNumber(String number) {
        this(getNumbArray(number), number.charAt(0) != '-');
    }

    public LongNumber abs() {
        return new LongNumber(numbers, true);
    }

    public int length() {
        return numbers.length;
    }

    public boolean isZero() {
        return (length() == 1 && numbers[0] == 0);
    }

    public static LongNumber zero() {
        return new LongNumber(new byte[]{0}, true);
    }

    private static byte[] getNumbArray(String number) {
        if (number == null || number.isEmpty()) {
            throw new IllegalArgumentException("Строка пуста");
        }
        int start = 0;
        if (number.charAt(0) == '-' || number.charAt(0) == '+') {
            start = 1;
            if (number.length() == 1) {
                throw new IllegalArgumentException("В строке нет цифр");
            }
        }
        int len = number.length() - start;
        byte[] numbers = new byte[len];
        for (int i = 0; i < len; i++) {
            char ch = number.charAt(number.length() - 1 - i);
            if (!Character.isDigit(ch)) {
                throw new IllegalArgumentException("В строке содержатся не числа");
            }
            numbers[i] = (byte) (ch - '0');
        }
        return removeExtraZeros(numbers);
    }

    public void print() {
        if (!isItPositive) System.out.print("-");
        for (int i = numbers.length - 1; i >= 0; i--) {
            System.out.print(numbers[i]);
        }
    }

    public byte isItMoreThan(LongNumber second) {
        if (this.isItPositive != second.isItPositive) {
            if (this.isItPositive) return 1;
            else return -1;
        }
        byte result = 0;
        if (this.numbers.length > second.numbers.length) result = 1;
        else if (this.numbers.length < second.numbers.length) result = -1;
        else {
            for (int i = this.numbers.length - 1; i >= 0; i--) {
                if (this.numbers[i] > second.numbers[i]) {
                    result = 1;
                    break;
                } else if (this.numbers[i] < second.numbers[i]) {
                    result = -1;
                    break;
                }
            }
        }
        if (!this.isItPositive) result *= -1;
        return result;
    }

    public LongNumber negate() {
        if (numbers.length == 1 && numbers[0] == 0) return new LongNumber(numbers, true);
        return new LongNumber(numbers, !isItPositive);
    }

    private static byte[] removeExtraZeros(byte[] nums) {
        int cutPoint = nums.length;
        for (int i = nums.length - 1; i > 0; i--) {
            if (nums[i] == 0) {
                cutPoint = i;
            } else {
                break;
            }
        }
        return Arrays.copyOf(nums, cutPoint);
    }

    private byte[] numbersArrSum(byte[] second) {
        byte[] result = numbers.length > second.length ? numbers.clone() : second.clone();
        byte[] less = numbers.length > second.length ? second : numbers; //Внимательно, только чтение
        int next = 0;
        for (int i = 0; (next != 0 || i < less.length) && i < result.length; i++) {
            int current = result[i] + next + (i < less.length ? less[i] : 0);
            result[i] = (byte) (current % 10);
            next = current / 10;
        }
        if (next != 0) {
            result = Arrays.copyOf(result, result.length + 1);
            result[result.length - 1] = (byte) next;
        }
        return result;
    }

    //из большего вычитаем меньшее
    private byte[] numbersArrDiff(byte[] second) {
        byte[] result = numbers.clone();
        int next = 0;
        for (int i = 0; (next != 0 || i < second.length) && i < result.length; i++) {
            int current = result[i] - next - (i < second.length ? second[i] : 0);
            next = 0;
            if (current < 0) {
                next = 1;
                current += 10;
            }
            result[i] = (byte) (current);
        }
        return removeExtraZeros(result);
    }

    public LongNumber sum(LongNumber second) {
        // 1. знаки одинаковые — просто складываем модули и сохраняем знак
        if (isItPositive == second.isItPositive) {
            return new LongNumber(numbersArrSum(second.numbers), isItPositive);
        }
        // 2. знаки разные — сравниваем модули, из большего вычитаем меньший и берём его знак
        return switch (abs().isItMoreThan(second.abs())) {
            case 1 -> new LongNumber(numbersArrDiff(second.numbers), isItPositive);
            case -1 -> new LongNumber(second.numbersArrDiff(numbers), second.isItPositive);
            default -> zero();
        };
    }


    public LongNumber sub(LongNumber second) {
        return sum(second.negate());
    }

    private LongNumber tenMultiply(int power) {
        if (power == 0 || isZero()) {
            return new LongNumber(numbers, isItPositive);
        }
        byte[] newNumbers = new byte[numbers.length + power];
        System.arraycopy(numbers, 0, newNumbers, power, numbers.length);
        return new LongNumber(newNumbers, isItPositive);
    }

    private LongNumber digitMultiply(int num) {
        if (num == 0) return new LongNumber(new byte[]{0}, true);
        if (num == 1) return new LongNumber(numbers, isItPositive);
        boolean newArePositive = isItPositive;
        if (num < 0) newArePositive = !newArePositive;
        byte[] newNumbers = numbers.clone();
        num = Math.abs(num);
        int next = 0;
        for (int i = 0; i < newNumbers.length; i++) {
            int curNumber = newNumbers[i] * num + next;
            newNumbers[i] = (byte) (curNumber % 10);
            next = curNumber / 10;
        }
        if (next != 0) {
            newNumbers = Arrays.copyOf(newNumbers, newNumbers.length + 1);
            newNumbers[newNumbers.length - 1] = (byte) next;
        }
        return new LongNumber(newNumbers, newArePositive);
    }

    public LongNumber multiply(LongNumber second) {
        if (this.isZero() || second.isZero()) {
            return zero();
        }
        LongNumber thisAbs = this.abs();
        LongNumber result = thisAbs.digitMultiply(second.numbers[0]);
        for (int i = 1; i < second.numbers.length; i++) {
            result = result.sum(thisAbs.digitMultiply(second.numbers[i]).tenMultiply(i));
        }
        boolean resultPositive = (this.isItPositive == second.isItPositive);
        return resultPositive ? result : result.negate();
    }

    //деление с остатком
    //возвращает под модулем
    public LongNumber[] getLongNumberMultiplyArr() {
        LongNumber[] result = new LongNumber[10];
        LongNumber thisAbs = abs();
        for (int i = 0; i < result.length; i++) {
            result[i] = thisAbs.digitMultiply(i);
        }
        return result;
    }

    private divideResult divideStep(LongNumber[] dividers) {
        LongNumber dividend = abs();
        LongNumber divisor = dividers[1];

        if (divisor.isZero()) {
            throw new ArithmeticException("division by zero");
        }

        int tenPower = dividend.length() - divisor.length();

        // dividend < divisor
        if (tenPower < 0) {
            return new divideResult(zero(), dividend);
        }

        //1003232
        // 213000
        //
        LongNumber finalDivisor = divisor.tenMultiply(tenPower);

        // D * 10^tenPower> N, пробуем на разряд ниже
        if (finalDivisor.isItMoreThan(dividend) == 1) { // finalDivisor > dividend
            tenPower--;
            if (tenPower < 0) {
                return new divideResult(zero(), dividend);
            }
            finalDivisor = divisor.tenMultiply(tenPower);
        }

        int id = 1;
        for (int i = 9; i >= 1; i--) {
            LongNumber candidate = dividers[i].tenMultiply(tenPower);
            if (candidate.isItMoreThan(dividend) != 1) { // candidate <= dividend
                id = i;
                finalDivisor = candidate;
                break;
            }
        }

        LongNumber remainder = dividend.sub(finalDivisor);

        LongNumber quotientChunk = new LongNumber(new byte[]{(byte) id}, true).tenMultiply(tenPower);

        return new divideResult(quotientChunk, remainder);
    }

    public divideResult division(LongNumber divider) {
        if (divider.isZero()) {
            throw new ArithmeticException("division by zero");
        }
        if (isZero()) {
            return new divideResult(zero(), zero());
        }
        if (!isItPositive || !divider.isItPositive)
            throw new IllegalArgumentException("делитель и делимое должны быть положительными");
        LongNumber[] dividers = divider.getLongNumberMultiplyArr();

        LongNumber divRemainder = new LongNumber(this);
        LongNumber divResult = zero();
        //пока делимое больше делителя
        while (divRemainder.isItMoreThan(divider) != -1) { // >= 0
            divideResult stepResult = divRemainder.divideStep(dividers);
            divResult = divResult.sum(stepResult.result);
            divRemainder = stepResult.remainder;
        }
        return new divideResult(divResult, divRemainder);
    }

    static public LongNumber one() {
        return new LongNumber(new byte[]{1}, true);
    }

    public LongNumber binaryPower(int power) {
        if (power < 0) throw new IllegalArgumentException("Степень должна быть положительной");
        if (power == 0) return one();
        LongNumber multiplier = new LongNumber(this);
        LongNumber result = one();
        while (power > 1) {
            if ((power % 2) == 1) {
                result = result.multiply(multiplier);
            }
            multiplier = multiplier.multiply(multiplier);
            power /= 2;
        }
        return result.multiply(multiplier);
    }
}

