package ru.com.m74.cubes.common;

import org.slf4j.Logger;

public class TimeLogger {
    private static final ThreadLocal<Long> millis = new ThreadLocal<>();
    private static final ThreadLocal<Logger> loggerThreadLocal = new ThreadLocal<>();

    public static void init(Logger logger) {
        loggerThreadLocal.set(logger);
        reset();
    }

    public static void reset() {
        millis.set(System.currentTimeMillis());
    }

    public static void debug(String msg) {
        loggerThreadLocal.get().debug(msg + ", time: " + (System.currentTimeMillis() - millis.get()));
        reset();
    }
}
