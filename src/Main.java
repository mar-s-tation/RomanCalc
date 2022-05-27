import java.util.Scanner;

public class Main {

    private static final String[] operations = {"\\+", "-", "\\*", "/"};
    private static Integer aInteger;
    private static Integer bInteger;
    private static byte mode = 0; // 0 - работаем с Integer, 1 - работаем с RomanInteger
    private static String operatorToSplitWith = "";

    public static String calc(String input) throws PrintExeption {
        Integer result;

        if (checkCorrectness(input)) {
            if (aInteger < 1 || aInteger > 10)
                throw new PrintExeption( "Неправильный формат данных: " + aInteger);
            if (bInteger < 1 || bInteger > 10)
                throw new PrintExeption( "Неправильный формат данных" + bInteger);

            result = makeOperation();

            switch (mode) {
                case 1:
                    if (result < 0) throw new PrintExeption( "В римской системе нет отрицательных чисел");
                    return RomanInteger.convertNumberToRoman(result);
                default:
                    return result.toString();
            }
        }
        else {
            throw new PrintExeption("Неправильный формат данных");
        }
    }

    public static boolean checkCorrectness(String input) throws PrintExeption {
        if (input.length() < 3)
            return false;

        // проверяем количество операндов
        int countOperations = 0;
        for (String op : operations) {
            String opWithoutSlash = op.replaceAll("\\\\", ""); // проверяем без "\", сплитим по "\*"
            if (input.contains(opWithoutSlash)) {
                countOperations++;
                operatorToSplitWith = op;
            }
        }
        if (countOperations > 1) {
            throw new PrintExeption("Формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");
        }

        // проверяем операторы
        String[] operands = input.split(operatorToSplitWith);
        if (operands.length > 2) {
            throw new PrintExeption("Формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");
        }
        try {
            aInteger = Integer.parseInt(operands[0]);
            bInteger = Integer.parseInt(operands[1]);
            mode = 0;
        } catch (Exception e) {
            RomanInteger aRomanInteger = new RomanInteger( operands[0].replaceAll(" ", "") );
            aInteger = aRomanInteger.getArabicNumber();
            RomanInteger bRomanInteger = new RomanInteger( operands[1].replaceAll(" ", "") );
            bInteger = bRomanInteger.getArabicNumber();
            mode = 1;
        }

        return true;
    }

    public static Integer makeOperation() throws PrintExeption {
        Integer result;
        if (operatorToSplitWith.equals(operations[0])) result = aInteger + bInteger;
        else if (operatorToSplitWith.equals(operations[1])) result = aInteger - bInteger;
        else if (operatorToSplitWith.equals(operations[2])) result = aInteger * bInteger;
        else {
            if (bInteger == 0) { // на будущее, если не будет ограничений по ТЗ
                throw new PrintExeption( "Деление на ноль недопустимо.");
            }
            result = aInteger / bInteger;
        }
        return result;
    }

    public static void main(String[] args) throws PrintExeption {
        Scanner reader = new Scanner(System.in);
        String input = reader.nextLine();
        reader.close();
        System.out.println(calc(input));
    }
}
