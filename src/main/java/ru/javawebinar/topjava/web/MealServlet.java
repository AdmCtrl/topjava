package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger LOG = getLogger(MealServlet.class);
    private final MealDao mealDao = new MealDaoImpl();

    public void init() {
        final List<Meal> MEAL_LIST = Arrays.asList(
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        MEAL_LIST.forEach(mealDao::add);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("redirect from meal --> action:" + request.getParameter("action"));

        String action = request.getParameter("action");
        if (action != null && action.equalsIgnoreCase("delete")) {
            int id = Integer.valueOf(request.getParameter("id"));
            mealDao.delete(id);
            response.sendRedirect("meals");
            return;
        }
        if (action != null && action.equalsIgnoreCase("update")) {
            int id = Integer.valueOf(request.getParameter("id"));
            request.setAttribute("mealFromServlet", mealDao.getById(id));
        }
        List<MealTo> mealWithExceeds = MealsUtil.getFilteredWithExcess(mealDao.getAll(),
                LocalTime.of(0, 0), LocalTime.of(23, 59), 2000);
        request.setAttribute("mealList", mealWithExceeds);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        request.setAttribute("formatter", formatter);
        request.getRequestDispatcher("meals.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        LOG.debug("post attributes");
        String id = request.getParameter("id");
        Meal meal = new Meal(LocalDateTime.parse(request.getParameter("dateTime")), request.getParameter("name"), Integer.valueOf(request.getParameter("calories")));
        if ((!id.isEmpty())) {
            meal.setId(Integer.valueOf(id));
            mealDao.update(meal);
        } else {
            mealDao.add(meal);
        }
        response.sendRedirect("meals");
    }
}