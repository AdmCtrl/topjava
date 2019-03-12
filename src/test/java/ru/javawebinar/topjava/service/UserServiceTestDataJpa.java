package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.MealTestData.MEALS;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles("datajpa")
public class UserServiceTestDataJpa extends AbstractUserServiceTest {

    @Test
    public void getUserWithMeals() throws Exception {
        User user = service.getUserWithMeals(USER_ID);
        assertMatch(user, USER);
        MealTestData.assertMatch(user.getMeals(), MEALS);
    }

    @Test(expected = NotFoundException.class)
    public void getUserWithMealsNotFound() throws Exception {
        service.getUserWithMeals(1);
    }
}
