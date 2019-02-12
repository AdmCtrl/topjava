package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDao {

    Meal add(Meal p);

    boolean delete(int p);

    Meal update(Meal p);

    Meal getById(int p);

    List<Meal> getAll();
}
