package org.example.task1.main;

import org.example.task1.service.FileHandler;
import org.example.task1.service.impl.FileHandlerImpl;

public class Main {
    public static void main(String[] args) {
        String inputDir = "src/main/resources/input";
        String outputFile = "src/main/resources/result/result_arithmetic";

        FileHandler fileHandler = new FileHandlerImpl(inputDir, outputFile);
        fileHandler.processFiles();
    }
}
