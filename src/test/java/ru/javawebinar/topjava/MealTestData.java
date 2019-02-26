package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int MEAL1_ID = START_SEQ + 2;
    public static final int ADMIN_MEAL_ID = START_SEQ + 8;
    public static final Meal MEAL1, MEAL2, MEAL3, MEAL4, MEAL5, MEAL6, ADMIN_MEAL;

    static {
        MEAL1 = new Meal(MEAL1_ID, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500);
        MEAL2 = new Meal(MEAL1_ID + 1, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000);
        MEAL3 = new Meal(MEAL1_ID + 2, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500);
        MEAL4 = new Meal(MEAL1_ID + 3, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 500);
        MEAL5 = new Meal(MEAL1_ID + 4, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 1000);
        MEAL6 = new Meal(MEAL1_ID + 5, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510);
        ADMIN_MEAL = new Meal(ADMIN_MEAL_ID, LocalDateTime.of(2015, Month.JUNE, 1, 14, 0), "Шаверма", 1510);
    }

    public static final List<Meal> MEALS = Arrays.asList(MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);

    public static Meal getCreated() {
        return new Meal(null, LocalDateTime.of(2015, Month.JUNE, 1, 18, 0), "New dinner", 400);
    }

    public static Meal getUpdated() {
        return new Meal(MEAL1_ID, MEAL1.getDateTime(), "ReEdit Breakfast", 600);
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "user");
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("user").isEqualTo(expected);
    }
}
