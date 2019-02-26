package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static java.util.stream.Collectors.toList;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void testDelete() {
        service.delete(MEAL1_ID, USER_ID);
        assertMatch(Arrays.asList(MEAL6, MEAL5, MEAL4, MEAL3, MEAL2)
                .stream().sorted((p1, p2) -> p2.getDateTime().compareTo(p1.getDateTime()))
                .collect(toList()), service.getAll(USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteNotFound() {
        service.delete(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void testSave() {
        Meal created = getCreated();
        service.create(created, USER_ID);
        assertMatch(Arrays.asList(created, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1), service.getAll(USER_ID));
    }

    @Test
    public void testGet() {
        assertMatch(service.get(MEAL1_ID, USER_ID), MEAL1);
    }

    @Test(expected = NotFoundException.class)
    public void testGetNotFound() {
        service.get(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void testUpdate() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(updated.getId(), USER_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateNotFound() {
        service.update(MEAL1, ADMIN_ID);
    }

    @Test
    public void testGetAll() {
        assertMatch(service.getAll(USER_ID), MEALS);
    }

    @Test
    public void testGetBetween() {
        assertMatch(Arrays.asList(MEAL3, MEAL2, MEAL1),
                service.getBetweenDates(
                        LocalDate.of(2015, Month.MAY, 30),
                        LocalDate.of(2015, Month.MAY, 30), USER_ID)
                        .stream().sorted((p1, p2) -> p2.getDateTime().compareTo(p1.getDateTime()))
                        .collect(toList()));
    }
    @Test
    public void getBetweenDateTimes(){
        assertMatch(service.getBetweenDateTimes(
                LocalDateTime.of(2015, Month.MAY, 30, 10, 0),
                LocalDateTime.of(2015, Month.MAY, 30, 13, 0), USER_ID),
                Arrays.asList(MEAL1, MEAL2)
                        .stream().sorted((p1, p2) -> p2.getDateTime().compareTo(p1.getDateTime()))
                        .collect(toList()));
    }
}