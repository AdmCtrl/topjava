package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDaoImpl;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger LOG = getLogger(MealServlet.class);
    private final MealDao mealDao = new MealDaoImpl();
    public static final List<Meal> MEAL_LIST = Arrays.asList(
            new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
            new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
            new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
            new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
            new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
            new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
    );

    public MealServlet() {
        mealDao.addMeal(MEAL_LIST);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("redirect from meal --> action:" + request.getParameter("action"));

        String action = request.getParameter("action");
        if (action != null && action.equalsIgnoreCase("delete")) {
            int id = Integer.valueOf(request.getParameter("id"));
            mealDao.deleteMeal(id);
            response.sendRedirect("meals");
            return;
        }

        if (action != null && action.equalsIgnoreCase("update")) {
            int id = Integer.valueOf(request.getParameter("id"));
            request.setAttribute("mealFromServlet", mealDao.getMealById(id));
        }

        List<MealTo> mealWithExceeds = MealsUtil.getFilteredWithExcess(mealDao.getAllMeals(),
                LocalTime.of(0, 0), LocalTime.of(23, 59), 2000);
        request.setAttribute("mealList", mealWithExceeds);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        request.setAttribute("formatter", formatter);


        request.getRequestDispatcher("meals.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        LOG.debug("post attributes: {}", printNiceMap(request.getParameterMap()));

        String action = request.getParameter("action");

        if (action != null && action.equalsIgnoreCase("update") && (!request.getParameter("id").isEmpty())) {
            int id = Integer.valueOf(request.getParameter("id"));
            String description = request.getParameter("name");
            int calories = Integer.valueOf(request.getParameter("calories"));
            LocalDate date = LocalDate.parse(request.getParameter("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalTime time = LocalTime.parse(request.getParameter("time"), DateTimeFormatter.ISO_TIME);
            Meal meal = new Meal(LocalDateTime.of(date, time), description, calories);
            meal.setId(id);
            mealDao.updateMeal(meal);
        } else {
            String description = request.getParameter("name");
            int calories = Integer.valueOf(request.getParameter("calories"));
            LocalDate date = LocalDate.parse(request.getParameter("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalTime time = LocalTime.parse(request.getParameter("time"), DateTimeFormatter.ISO_TIME);

            mealDao.addMeal(new Meal(LocalDateTime.of(date, time), description, calories));
        }

        response.sendRedirect("meals");
    }

    private String printNiceMap(Map<String, String[]> map) {
        return map.entrySet().stream()
                .map(es -> es.getKey() + "->" + Arrays.deepToString(es.getValue()))
                .collect(Collectors.joining(";"));
    }
}