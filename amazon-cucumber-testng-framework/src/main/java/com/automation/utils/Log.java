package com.automation.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Log {

    private static final Logger LOGGER = LogManager.getLogger(Log.class);

    private Log() {
    }

    public static void info(String message) {
        LOGGER.info("[Thread: {}] {}", Thread.currentThread().getId(), message);
    }

    public static void warn(String message) {
        LOGGER.warn("[Thread: {}] {}", Thread.currentThread().getId(), message);
    }

    public static void error(String message) {
        LOGGER.error("[Thread: {}] {}", Thread.currentThread().getId(), message);
    }

    public static void error(String message, Throwable throwable) {
        LOGGER.error("[Thread: {}] {} - Exception: {}", Thread.currentThread().getId(), message, throwable.getMessage(), throwable);
    }

    public static void debug(String message) {
        LOGGER.debug("[Thread: {}] {}", Thread.currentThread().getId(), message);
    }

    public static void fatal(String message) {
        LOGGER.fatal("[Thread: {}] {}", Thread.currentThread().getId(), message);
    }
}
