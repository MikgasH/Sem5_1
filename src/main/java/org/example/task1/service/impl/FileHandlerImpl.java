package org.example.task1.service.impl;

import org.example.task1.arithmetic.ArithmeticProcessor;
import org.example.task1.arithmetic.impl.ArithmeticProcessorImpl;
import org.example.task1.service.FileHandler;
import org.example.task1.singleton.LoggerSingleton;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class FileHandlerImpl implements FileHandler {
    private static final int THREAD_POOL_SIZE = 4;
    private static final String INPUT_FILE_PATTERN = "data_*";
    private static final String NUMBER_DELIMITER = "\\s+";
    private static final String INVALID_FORMAT_ERROR = "Invalid file format: ";

    private final Path inputDir;
    private final Path outputFile;

    public FileHandlerImpl(String inputDir, String outputFile) {
        this.inputDir = Paths.get(inputDir);
        this.outputFile = Paths.get(outputFile);
    }

    @Override
    public void processFiles() {
        LoggerSingleton logger = LoggerSingleton.getInstance();
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        List<Future<String>> results = new ArrayList<>();

        try {
            if (!Files.exists(outputFile.getParent())) {
                Files.createDirectories(outputFile.getParent());
            }

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(inputDir, INPUT_FILE_PATTERN)) {
                for (Path file : stream) {
                    results.add(executor.submit(() -> processFile(file)));
                }
            }

            List<String> outputLines = new ArrayList<>();
            for (Future<String> result : results) {
                outputLines.add(result.get());
            }
            Files.write(outputFile, outputLines);
            logger.logInfo("Results written to: " + outputFile);

        } catch (IOException | InterruptedException | ExecutionException e) {
            logger.logError("Error: " + e.getMessage());
        } finally {
            executor.shutdown();
        }
    }

    private String processFile(Path file) {
        LoggerSingleton logger = LoggerSingleton.getInstance();
        try {
            List<String> lines = Files.readAllLines(file);

            if (lines.size() < 2) {
                logger.logError(INVALID_FORMAT_ERROR + file.getFileName());
                return INVALID_FORMAT_ERROR + file.getFileName();
            }

            int operation = Integer.parseInt(lines.get(0).trim());
            List<Double> numbers = Arrays.stream(lines.get(1).split(NUMBER_DELIMITER))
                    .map(Double::parseDouble)
                    .collect(Collectors.toList());

            ArithmeticProcessor processor = new ArithmeticProcessorImpl(operation, numbers);
            double result = processor.call();
            return "File: " + file.getFileName() + " Result: " + result;

        } catch (Exception e) {
            logger.logError("Error processing file: " + file.getFileName());
            return "File: " + file.getFileName() + " Error";
        }
    }
}
