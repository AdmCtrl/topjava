package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepositoryImpl.class);

    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, new Random().nextInt(2) + 1));
        log.info("init mock user-meals repository");

    }


    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {} for userId - {}", meal, userId);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            Map<Integer, Meal> userMeal = repository.computeIfAbsent(userId, (k) -> new ConcurrentHashMap<>());
            return userMeal.put(meal.getId(), meal);
        }
        // treat case: update, but absent in storage
        Map<Integer, Meal> userMeal = repository.get(userId);
        if (userMeal != null) {
            return userMeal.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        }
        return null;
    }

    @Override
    public boolean delete(int userId, int mealId) {
        log.info("User: " + userId + " meal list size:  " + repository.get(userId).size() + " id counter = " + counter.get());
        Meal userMeal = repository.get(userId).remove(mealId);
        log.info("User: " + userId + " delete meal " + mealId);
        log.info("User: " + userId + " meal list size:  " + repository.get(userId).size() + " id counter = " + counter.get());
        return userMeal != null;
    }

    @Override
    public Meal get(int mealId, int userId) {
        log.info("get by id {} for userId - {}", mealId, userId);
        Map<Integer, Meal> userMeal = repository.get(userId);
        if (userMeal != null) {
            return userMeal.get(mealId);
        }
        return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll for userId - {}", userId);
        Map<Integer, Meal> userMeal = repository.get(userId);
        if (userMeal != null) {
            return sorting(new ArrayList<>(userMeal.values()));
        }
        return Collections.emptyList();
    }

    @Override
    public List<Meal> getAllFilter(int userId, LocalDate startDate, LocalTime startTime, LocalDate endDate,
                                   LocalTime endTime) {
        log.info("getAllFilter for userId - {}. filter - {}. {}. {}. {}", userId, startDate, startTime, endDate, endTime);
        Map<Integer, Meal> userMeal = repository.get(userId);
        if (userMeal != null) {
            return sorting(userMeal.values().stream()
                    .filter(x -> DateTimeUtil.isBetween(x.getDate(), startDate, endDate))
                    .filter(x -> DateTimeUtil.isBetween(x.getTime(), startTime, endTime))
                    .collect(Collectors.toList()));
        }
        return Collections.emptyList();
    }

    private List<Meal> sorting(List<Meal> meals) {
        Comparator<Meal> mealComparator
                = Comparator.comparing(Meal::getDateTime, Collections.reverseOrder());
        meals.sort(mealComparator);
        return meals;
    }

}

