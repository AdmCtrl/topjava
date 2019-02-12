package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDaoImpl implements MealDao {


    private Map<Integer, Meal> meals = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public Meal add(Meal p) {
        p.setId(counter.incrementAndGet());
        meals.put(p.getId(), p);
        return p;
    }

    @Override
    public boolean delete(int p) {
        Meal meal = meals.get(p);
        if (meal != null && meal.getId() == p) {
            meals.remove(p);
            return true;
        }
        return false;
    }

    @Override
    public Meal update(Meal p) {
        return meals.computeIfPresent(p.getId(), (id, oldMeal) -> p);
    }

    @Override
    public Meal getById(int p) {
        Meal meal = meals.get(p);
        return meal != null ? meal : null;
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList(meals.values());
    }
}
