package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private ClassPathXmlApplicationContext ctx;
    private MealRestController mealController;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ctx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        mealController = ctx.getBean(MealRestController.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        switch (request.getParameter("action") == null ? "filter" : request.getParameter("action")) {
            case "update":
                Meal meal = new Meal((request.getParameter("id") != null &&
                        !request.getParameter("id").isEmpty()) ? Integer.valueOf(request.getParameter("id")) : null,
                        LocalDateTime.parse(request.getParameter("dateTime")),
                        request.getParameter("description"),
                        Integer.parseInt(request.getParameter("calories")));
                log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
                if (meal.isNew()) {
                    mealController.create(meal);
                } else {
                    mealController.update(meal, meal.getId());
                }
                response.sendRedirect("meals");
                break;
            case "filter":
                List<MealTo> meals;
                if (!(request.getParameter("startDate") == null) && !(request.getParameter("startTime") == null) &&
                        !(request.getParameter("endDate") == null) && !(request.getParameter("endTime") == null)) {
                    LocalDate startDate = LocalDate.parse(request.getParameter("startDate"));
                    LocalDate endDate = LocalDate.parse(request.getParameter("endDate"));
                    LocalTime startTime = LocalTime.parse(request.getParameter("startTime"));
                    LocalTime endTime = LocalTime.parse(request.getParameter("endTime"));
                    meals = mealController.getAllFilter(startDate, startTime, endDate, endTime);
                } else {
                    meals = mealController.getAll();
                }
                request.setAttribute("meals", meals);
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                mealController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        mealController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals", mealController.getAll());
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    @Override
    public void destroy() {
        if (ctx != null) ctx.close();
    }
}