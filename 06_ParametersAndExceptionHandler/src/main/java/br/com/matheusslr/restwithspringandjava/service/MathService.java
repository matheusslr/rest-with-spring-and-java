package br.com.matheusslr.restwithspringandjava.service;

import br.com.matheusslr.restwithspringandjava.exceptions.UnsupportedMathOperationException;
import org.springframework.stereotype.Service;

import static br.com.matheusslr.restwithspringandjava.utils.MathUtils.convertStringToDouble;

@Service
public class MathService {

    public Double sum(String numberOne, String numberTwo) {
        if (numberOne == null || numberTwo == null) {
            throw new UnsupportedMathOperationException("Input value cannot be null");
        }
        Double numberOneConverted = convertStringToDouble(numberOne);
        Double numberTwoConverted = convertStringToDouble(numberTwo);

        return numberOneConverted + numberTwoConverted;
    }

    public Double sub(String numberOne, String numberTwo) {
        if (numberOne == null || numberTwo == null) {
            throw new UnsupportedMathOperationException("Input value cannot be null");
        }
        Double numberOneConverted = convertStringToDouble(numberOne);
        Double numberTwoConverted = convertStringToDouble(numberTwo);

        return numberOneConverted - numberTwoConverted;
    }

    public Double mult(String numberOne, String numberTwo) {
        if (numberOne == null || numberTwo == null) {
            throw new UnsupportedMathOperationException("Input value cannot be null");
        }
        Double numberOneConverted = convertStringToDouble(numberOne);
        Double numberTwoConverted = convertStringToDouble(numberTwo);

        return numberOneConverted * numberTwoConverted;
    }

    public Double division(String numberOne, String numberTwo) {
        if (numberOne == null || numberTwo == null) {
            throw new UnsupportedMathOperationException("Input value cannot be null");
        }
        Double numberOneConverted = convertStringToDouble(numberOne);
        Double numberTwoConverted = convertStringToDouble(numberTwo);

        if (numberTwoConverted == 0) {
            throw new UnsupportedMathOperationException("Undefined divides 0 operation");
        }

        return numberOneConverted / numberTwoConverted;
    }

    public Double mean(String numberOne, String numberTwo) {
        if (numberOne == null || numberTwo == null) {
            throw new UnsupportedMathOperationException("Input value cannot be null");
        }
        Double numberOneConverted = convertStringToDouble(numberOne);
        Double numberTwoConverted = convertStringToDouble(numberTwo);

        return (numberOneConverted + numberTwoConverted) / 2;
    }

    public Double squareRoot(String number) {
        if (number == null) {
            throw new UnsupportedMathOperationException("Input value cannot be null");
        }
        Double numberConverted = convertStringToDouble(number);

        return Math.sqrt(numberConverted);
    }
}
