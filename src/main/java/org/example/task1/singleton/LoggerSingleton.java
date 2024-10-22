package org.example.task1.singleton;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerSingleton {
    private static LoggerSingleton instance;
    private final Logger logger;

    private LoggerSingleton() {
        logger = LogManager.getLogger(LoggerSingleton.class);
    }

    public static synchronized LoggerSingleton getInstance() {
        if (instance == null) {
            instance = new LoggerSingleton();
        }
        return instance;
    }

    public void logInfo(String message) {
        logger.info(message);
    }

    public void logError(String message) {
        logger.error(message);
    }
}
