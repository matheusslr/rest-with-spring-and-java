package br.com.matheusslr.restwithspringandjava.utils;

import br.com.matheusslr.restwithspringandjava.exceptions.UnsupportedMathOperationException;

public class MathUtils {
    public static Double convertStringToDouble(String value) {
        if (!isNumeric(value)) throw new UnsupportedMathOperationException("Input must be a number");
        String number = value.replaceAll(",", ".");
        return Double.parseDouble(number);
    }

    public static boolean isNumeric(String strNumber) {
        return strNumber.matches("[-+]?[0-9]*[\\.,]?[0-9]+");
    }
}
