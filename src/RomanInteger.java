import org.jetbrains.annotations.NotNull;

import java.util.Enumeration;
import java.util.Hashtable;
public class RomanInteger {
//    String number;
    private static Integer arabicNumber;

    private static Hashtable<Character, Integer> alphabet = new Hashtable<Character, Integer>();

    public RomanInteger(@NotNull String numString) throws PrintExeption {
        initAlphabet();
        arabicNumber = convertNumberToArabic(numString);
    }

    public static void initAlphabet() {
        alphabet.put('I', 1);
        alphabet.put('V', 5);
        alphabet.put('X', 10);
        alphabet.put('L', 50);
        alphabet.put('C', 100);
        alphabet.put('D', 500);
        alphabet.put('M', 1000);
    }

    public static Integer convertNumberToArabic(@NotNull String numString) throws PrintExeption {
        char[] numChars = numString.toCharArray();

//        Если нет символов из алфавита
        String patternString = "IVXLCDM";
        for (Character letter: numChars) {
            if (!patternString.contains(letter.toString()))
            {
                throw new PrintExeption("Строка должна состоять из целых римских или арабских чисел");
            }
        }

        Integer prev = alphabet.get(numChars[0]), next;
        Integer summ = alphabet.get(numChars[0]); // запишем первое число сразу
        if (numChars.length == 1) {
            return summ;
        }

        for (int numIndex=1; numIndex < numChars.length; numIndex++) {
            next = alphabet.get(numChars[numIndex]);
            if (prev == next) {
                summ += next;
            }

            if (prev < next) {
                summ += next - 2*prev;
                // MIX : (M+I)+X-I-I = M+X-I
                // XIV : (X+I)+V-I-I = X+V-I
                // XL  : (L)+X-L-L = X-L
                // XCIX: ( ((X)+C-X-X)+I )+X-I-I

                if (summ < 0) { // IVX = (V-I)+X-(V-I)-(V-I) - таких чисел не бывает
                    String errorMessage = "Неправильный формат числа: " + numString;
                    throw new PrintExeption(errorMessage);
                }
            }

            if (prev > next) {
                summ += next;
            }
            prev = next;
        }

        return summ;
    }

    public static Integer getArabicNumber() {
        return arabicNumber;
    }

    public static String convertNumberToRoman(Integer number) {
        initAlphabet();

        String result = "";
        result = getDigit(number);

        return result;
    }

    public static String getDigit(Integer number) {
        String result = "";
        int modValue;

        int numberLength = (int) (Math.log10(number) + 1);
        if (numberLength == 1) modValue=number;
        else modValue =  (number / (int)Math.pow((double)10, (double)(numberLength-1)));

        Integer roundNumber;

        if (modValue == 4 || modValue == 9) {
            Enumeration<Character> enumOfKeys = alphabet.keys();

            Character minLimit = 'I';
            Character maxLimit = 'I';
            int min = 1;
            int max = 100000;
            while (enumOfKeys.hasMoreElements()) {
                Character key = enumOfKeys.nextElement();
                Integer alphabetValue = alphabet.get(key);

                if ((number < alphabetValue) && (alphabetValue < max)) {
                    max = alphabetValue;
                    maxLimit = key;
                }
            }

            roundNumber = modValue*(int)Math.pow((double)10, (double)(numberLength-1));
            enumOfKeys = alphabet.keys(); // проходим еще раз, чтобы вычислить вычитаемое
            while (enumOfKeys.hasMoreElements()) {
                Character key = enumOfKeys.nextElement();
                Integer alphabetValue = alphabet.get(key);

                if ((max - alphabetValue) == roundNumber) {
                    minLimit = key;
                }
            }

            result = minLimit.toString() + maxLimit.toString();
        }
        else if (modValue == 6 || modValue == 7 || modValue == 8) {
            Enumeration<Character> enumOfKeys = alphabet.keys();

            Character minLimit = 'I';
            Character maxLimit = 'I';
            int min = 1;
            int max = 1;
            while (enumOfKeys.hasMoreElements()) {
                Character key = enumOfKeys.nextElement();
                Integer alphabetValue = alphabet.get(key);

                if ((number > alphabetValue) && (alphabetValue > max)) {
                    max = alphabetValue;
                    maxLimit = key;
                }
            }


            roundNumber = modValue*(int)Math.pow((double)10, (double)(numberLength-1));
            Integer fix = (roundNumber - max);
            String suffix = "";
            enumOfKeys = alphabet.keys(); // проходим еще раз, чтобы вычислить прибавляемое
            while (enumOfKeys.hasMoreElements()) {
                Character key = enumOfKeys.nextElement();
                Integer alphabetValue = alphabet.get(key);

                if ((fix >= alphabetValue) && (alphabetValue > min)) {
                    min = alphabetValue;
                    minLimit = key;
                }
            }
            Integer counter = 1;
            while (counter*min <= fix) {
                counter++;
                suffix += minLimit;
            }

            result = maxLimit.toString() + suffix;
        }
        else {
            Enumeration<Character> enumOfKeys = alphabet.keys();

            Character minLimit = 'I';
            int min = 1;
            while (enumOfKeys.hasMoreElements()) {
                Character key = enumOfKeys.nextElement();
                Integer alphabetValue = alphabet.get(key);

                if ((number >= alphabetValue) && (alphabetValue > min)) {
                    min = alphabetValue;
                    minLimit = key;
                }
            }

            Integer counter = 1;
            result += minLimit;
            if (numberLength == 1) {
                int tail = (number - min);
                minLimit = 'I';
                min = 1;
                while (counter*min <= tail) {
                    counter++;
                    result += minLimit;
                }

                return result;
            }
            else {
                roundNumber = modValue*(int)Math.pow((double)10, (double)(numberLength-1));;
                while (counter*min < roundNumber) {
                    counter++;
                    result += minLimit;
                }
            }

        }

        // прибавляем остаток в младших разрядах
        Integer modulo = number-roundNumber;
        if (modulo > 0)
            result += getDigit(modulo);

        return result;
    }

    public static void main(String[] args) throws PrintExeption {
//        String input= "VI";
//        RomanInteger testNumber = new RomanInteger(input);
//        System.out.println("Test:" + testNumber.getArabicNumber());

        for (int i = 0; i<1000; i++)
        {
            System.out.println(i + " -> " + convertNumberToRoman(i));
        }
    }
}
