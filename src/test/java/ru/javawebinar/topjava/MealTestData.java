package ru.javawebinar.topjava;

import org.junit.Assert;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final TestEquate<Meal> EQUATE = new TestEquate<>();
    public static final int MEAL1_ID = START_SEQ + 2;
    public static final int ADMIN_MEAL_ID = START_SEQ + 8;
    public static final Meal MEAL1,MEAL2,MEAL3,MEAL4,MEAL5,MEAL6,ADMIN_MEAL;

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

    public static class TestEquate<T> {
        private Equality<T> equality;

        public TestEquate() {
            this((T p1, T p2) -> p1 == p2 || String.valueOf(p1).equals(String.valueOf(p2)));
        }

        public TestEquate(Equality<T> equality) {
            this.equality = equality;
        }

        public void assertEquals(T p1, T p2) {
            Assert.assertEquals(wrap(p1), wrap(p2));
        }

        public void assertCollectionEquals(Collection<T> p1, Collection<T> p2) {
            Assert.assertEquals(wrap(p1), wrap(p2));
        }

        public Wrapper wrap(T entity) {
            return new Wrapper(entity);
        }

        public List<Wrapper> wrap(Collection<T> collection) {
            return collection.stream().map(this::wrap).collect(Collectors.toList());
        }

        public interface Equality<T> {
            boolean areEqual(T p1, T p2);
        }

        private class Wrapper {
            private T entity;

            private Wrapper(T entity) {
                this.entity = entity;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Wrapper that = (Wrapper) o;
                return entity != null ? equality.areEqual(entity, that.entity) : that.entity == null;
            }

            @Override
            public String toString() {
                return String.valueOf(entity);
            }
        }
    }
}
