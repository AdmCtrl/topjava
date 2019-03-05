package ru.javawebinar.topjava.util;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import ru.javawebinar.topjava.web.UserServlet;

import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

public class StopWatchForTest extends Stopwatch {
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    private static final Logger log = getLogger(UserServlet.class);

    private static void logInfo(Description description, String status, long nanos) {
        String testName = description.getMethodName();
        log.debug("\n{}Test{} [{} {}],{} spent: {}{} milliseconds{}", ANSI_YELLOW, ANSI_GREEN, testName, status, ANSI_YELLOW, ANSI_GREEN,
                TimeUnit.NANOSECONDS.toMillis(nanos), ANSI_RESET);
    }

    @Override
    protected void finished(long nanos, Description description) {
        logInfo(description, "FINISHED", nanos);
    }
}
