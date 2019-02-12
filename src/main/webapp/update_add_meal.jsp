<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<p>
<c:choose>
    <c:when test="${empty mealFromServlet.id}">
        <h2>Add new meal</h2>
    </c:when>
    <c:otherwise>
        <h2>Update meal Id:${mealFromServlet.id}</h2>
    </c:otherwise>
</c:choose>
</p>

<form action="<c:url value="/meals"/>" method="post">
    <input type="hidden" name="action" value="update">
    <input type="hidden" name="id" value="${mealFromServlet.id}">
    <table>
        <tr>
            <td><label for="name">Description: </label></td>
            <td><input type="text" id="name" name="name" value="${mealFromServlet.description}" required/></td>
        </tr>
        <tr>
            <td><label for="calories">Calories:</label></td>
            <td><input type="number" id="calories" name="calories" placeholder="number of calories" min="0"
                       value="${mealFromServlet.calories}" required/></td>
        </tr>
        <tr>
            <td><label for="date">Date</label></td>
            <td><input type="date" id="date" name="date" value="${mealFromServlet.dateTime.toLocalDate()}" required/></td>
        </tr>
        <tr>
            <td><label for="time">Time</label></td>
            <td><input type="time" id="time" name="time" value="${mealFromServlet.dateTime.toLocalTime()}" required/></td>
        </tr>
    </table>
    <button type="submit">Submit</button>
</form>

