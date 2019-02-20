package ru.javawebinar.topjava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDateTime;
import java.util.Arrays;

public class SpringMain {
    private static final Logger log = LoggerFactory.getLogger(SpringMain.class);

    public static void main(String[] args) {
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ROLE_ADMIN));
            log.info(Arrays.toString(appCtx.getBeanDefinitionNames()));
            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            mealRestController.create(new Meal(LocalDateTime.of(2001, 11, 25, 11, 15), "blabla", 20));
            mealRestController.create(new Meal(LocalDateTime.of(2001, 11, 25, 11, 13), "alabla", 21));
            mealRestController.create(new Meal(LocalDateTime.of(2001, 11, 25, 11, 14), "clabla", 22));
            mealRestController.create(new Meal(LocalDateTime.of(2001, 11, 25, 11, 14), "clabla", 22));
            log.info(String.valueOf(mealRestController.getAll()));
            log.info(mealRestController.get(3).toString());
            mealRestController.delete(2);
            mealRestController.update(new Meal(LocalDateTime.of(2001, 11, 26, 11, 13), "dlabla", 22222), 3);
            log.info(String.valueOf(mealRestController.getAll()));
            log.info(mealRestController.get(4).toString());
            mealRestController.delete(4);
            mealRestController.update(new Meal(LocalDateTime.of(2001, 11, 26, 11, 13), "dlabla", 22222), 3);
            log.info(mealRestController.get(3).toString());
            log.info(String.valueOf(mealRestController.getAll()));
            log.info(String.valueOf(mealRestController.getAll()));
            log.info(mealRestController.get(6).toString());
            mealRestController.delete(6);
            mealRestController.update(new Meal(LocalDateTime.of(2001, 11, 26, 11, 13), "dlabla", 22222), 10);
            mealRestController.create(new Meal(LocalDateTime.of(2001, 11, 26, 11, 13), "dlabla", 22222));
            log.info(mealRestController.get(10).toString());
        }
    }
}
