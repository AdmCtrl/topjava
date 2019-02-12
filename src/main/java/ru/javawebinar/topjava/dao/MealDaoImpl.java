package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDaoImpl implements MealDao {

    private List<Meal> meals;
    private AtomicInteger counter;

    public MealDaoImpl() {
        meals = Collections.synchronizedList(new ArrayList<>());
        counter = new AtomicInteger(0);
    }

    @Override
    public void addMeal(List<Meal> meal) {
        meal.forEach(this::addMeal);
    }

    @Override
    public void addMeal(Meal meal) {
        meal.setId(counter.getAndIncrement());
        meals.add(meal);
    }

    @Override
    public void deleteMeal(int mealId) {
        meals.removeIf(m -> m.getId() == mealId);
    }

    @Override
    public synchronized void updateMeal(Meal newMeal) {
        Meal oldMeal = meals.stream().filter(m -> m.getId() == newMeal.getId()).findFirst().orElse(null);
        if (oldMeal != null) {
            newMeal.setId(oldMeal.getId());
            deleteMeal(oldMeal.getId());
            meals.add(newMeal);
        }
    }

    @Override
    public Meal getMealById(int mealId) {
        return meals.stream().filter(m -> m.getId() == mealId).findFirst().orElse(null);
    }

    @Override
    public List<Meal> getAllMeals() {
        return new ArrayList<>(meals);
    }
}
