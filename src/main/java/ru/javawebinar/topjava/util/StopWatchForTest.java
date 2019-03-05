package ru.javawebinar.topjava.util;

import org.junit.AssumptionViolatedException;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;

import java.util.concurrent.TimeUnit;

public class StopWatchForTest extends Stopwatch {
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";

    private static void logInfo(Description description, String status, long nanos) {
        String testName = description.getMethodName();
        System.out.println(String.format("Test %s %s, spent %d microseconds",
                testName, status, TimeUnit.NANOSECONDS.toMicros(nanos)));
    }

    @Override
    protected void succeeded(long nanos, Description description) {
        logInfo(description, ANSI_GREEN + "SUCCEEDED" + ANSI_RESET, nanos);
    }

    @Override
    protected void failed(long nanos, Throwable e, Description description) {
        logInfo(description, ANSI_RED + "FAILED" + ANSI_RESET, nanos);
    }

    @Override
    protected void skipped(long nanos, AssumptionViolatedException e, Description description) {
        logInfo(description, ANSI_YELLOW + "SKIPPED" + ANSI_RESET, nanos);
    }

    @Override
    protected void finished(long nanos, Description description) {
        logInfo(description, ANSI_BLUE + "FINISHED" + ANSI_RESET, nanos);
    }
}
