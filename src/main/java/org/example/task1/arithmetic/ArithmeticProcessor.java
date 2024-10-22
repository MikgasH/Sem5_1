package org.example.task1.arithmetic;

import java.util.concurrent.Callable;

public interface ArithmeticProcessor extends Callable<Double> {
    @Override
    Double call();
}
