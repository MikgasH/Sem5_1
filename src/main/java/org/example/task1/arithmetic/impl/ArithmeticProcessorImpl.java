package org.example.task1.arithmetic.impl;

import org.example.task1.arithmetic.ArithmeticProcessor;
import org.example.task1.singleton.LoggerSingleton;

import java.util.List;

public class ArithmeticProcessorImpl implements ArithmeticProcessor {
    private final int operation;
    private final List<Double> numbers;

    public ArithmeticProcessorImpl(int operation, List<Double> numbers) {
        this.operation = operation;
        this.numbers = numbers;
    }

    @Override
    public Double call() {
        LoggerSingleton logger = LoggerSingleton.getInstance();
        double result;

        switch (operation) {
            case 1:
                result = numbers.stream().mapToDouble(Double::doubleValue).sum();
                break;
            case 2:
                result = numbers.stream().reduce(1.0, (a, b) -> a * b);
                break;
            case 3:
                result = numbers.get(0) - numbers.subList(1, numbers.size()).stream()
                        .mapToDouble(Double::doubleValue).sum();
                break;
            case 4:
                result = numbers.get(0);
                for (int i = 1; i < numbers.size(); i++) {
                    if (numbers.get(i) == 0) throw new ArithmeticException("Division by zero");
                    result /= numbers.get(i);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid operation");
        }

        logger.logInfo("Operation " + operation + " completed. Result: " + result);
        return result;
    }
}
