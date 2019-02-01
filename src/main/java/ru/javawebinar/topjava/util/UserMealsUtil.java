package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000)
                .forEach(System.out::println);
        System.out.println();
        getFilteredWithExceededOption(mealList, LocalTime.of(10, 0), LocalTime.of(20, 0), 2000)
                .forEach(System.out::println);
        System.out.println();
        getFilteredWithExceededTwoOption(mealList, LocalTime.of(10, 0), LocalTime.of(20, 0), 2000)
                .forEach(System.out::println);
        System.out.println();
        getFilteredWithExceededThreeOption(mealList, LocalTime.of(10, 0), LocalTime.of(20, 0), 2000)
                .forEach(System.out::println);
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesRowMeals = new HashMap<>();
        for (UserMeal userMeal : mealList) {
            caloriesRowMeals.merge(userMeal.getDateTime().toLocalDate(), userMeal.getCalories(), Integer::sum);
        }
        List<UserMealWithExceed> list = new ArrayList<>();
        for (UserMeal userMealRow : mealList) {
            if (TimeUtil.isBetween(userMealRow.getDateTime().toLocalTime(), startTime, endTime)) {
                UserMealWithExceed userMealWithExceed = new UserMealWithExceed(userMealRow.getDateTime(), userMealRow.getDescription(), userMealRow.getCalories(), caloriesRowMeals.get(userMealRow.getDateTime().toLocalDate()) > caloriesPerDay);
                list.add(userMealWithExceed);
            }
        }
        return list;
    }

    public static List<UserMealWithExceed> getFilteredWithExceededOption(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesRowMeals = mealList.stream()
                .collect(Collectors.groupingBy(userMealRow -> userMealRow.getDateTime().toLocalDate(), Collectors.summingInt(UserMeal::getCalories)));
        return mealList.stream()
                .filter(userMealRow -> TimeUtil.isBetween(userMealRow.getDateTime().toLocalTime(), startTime, endTime))
                .map(userMealRow -> new UserMealWithExceed(userMealRow.getDateTime(), userMealRow.getDescription(), userMealRow.getCalories(), caloriesRowMeals.get(userMealRow.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExceed> getFilteredWithExceededTwoOption(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return mealList.stream()
                .collect((Collectors.groupingBy(userMealRow -> userMealRow.getDateTime().toLocalDate())))
                .values()
                .stream()
                .flatMap(mealsEach -> mealsEach.stream()
                        .filter(userMealRow -> TimeUtil.isBetween(userMealRow.getDateTime().toLocalTime(), startTime, endTime))
                        .map(userMealRow -> new UserMealWithExceed(userMealRow.getDateTime(), userMealRow.getDescription(), userMealRow.getCalories(), mealsEach.stream().mapToInt(UserMeal::getCalories).sum() > caloriesPerDay)))
                .collect(toList());
    }

    public static List<UserMealWithExceed> getFilteredWithExceededThreeOption(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesRowMeals = new HashMap<>();
        List<UserMealWithExceed> list = new ArrayList<>();
        List<UserMeal> listUserMeal = new ArrayList<>();
        LocalDate dateRow = null;
        for (UserMeal aMealList : mealList) {
            if (dateRow == null) dateRow = aMealList.getDateTime().toLocalDate();
            if (dateRow.compareTo(aMealList.getDateTime().toLocalDate()) < 0) {
                for (UserMeal userMeal : listUserMeal)
                    list.add(new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), caloriesRowMeals.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay));
                listUserMeal.clear();
                dateRow = aMealList.getDateTime().toLocalDate();
            }
            caloriesRowMeals.merge(aMealList.getDateTime().toLocalDate(), aMealList.getCalories(), Integer::sum);
            if (TimeUtil.isBetween(aMealList.getDateTime().toLocalTime(), startTime, endTime))
                listUserMeal.add(aMealList);
        }
        for (UserMeal userMeal : listUserMeal)
            list.add(new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), caloriesRowMeals.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay));
        return list;
    }
}