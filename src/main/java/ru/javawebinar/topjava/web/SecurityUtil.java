package ru.javawebinar.topjava.web;

import java.util.concurrent.atomic.AtomicInteger;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {

    public static int ADMIN_ID = 1;
    public static int USER_ID = 2;

    private static AtomicInteger authUserId = new AtomicInteger(1);

    public static int authUserId() {
        return authUserId.get();
    }

    public static void setAuthUserId(int id) {
        authUserId.set(id);
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }
}