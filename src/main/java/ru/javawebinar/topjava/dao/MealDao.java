package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDao {
    void addMeal(List<Meal> meal);

    void addMeal(Meal meal);

    void deleteMeal(int mealId);

    void updateMeal(Meal meal);

    Meal getMealById(int mealId);

    List<Meal> getAllMeals();
}
