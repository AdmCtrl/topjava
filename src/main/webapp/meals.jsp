<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>

<html>
<p>
    <a href="<c:url value="/"/>">back</a>
</p>
<th align="left"><input type="submit" VALUE="Add Meal" id="buttn"/></th>
<table width="300" cellspacing="0" cellpadding="4" border="1">
    <tr>
        <td width="300" height="200">
            <table id="addform" style="display: none" cellpadding="4" cellspacing="0">
                <form action="<c:url value="/meals"/>" method="post">
                    <td>
                        <jsp:include page='update_add_meal.jsp'/>
                    </td>
                </form>
            </table>
        </td>
    </tr>
</table>
<h2>Meals</h2>
<body>
<table border="1" cellpadding="8" cellspacing="0">
    <thead>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
    </thead>
    <c:forEach var="mealFromThisMealList" items="${mealList}">
        <tr style="${mealFromThisMealList.excess ? 'color: red' : 'color: green'}">
            <td>${mealFromThisMealList.dateTime.toLocalDate()} ${mealFromThisMealList.dateTime.toLocalTime()}</td>
            </td>
            <td><c:out value="${mealFromThisMealList.description}"/>
            </td>
            <td><c:out value="${mealFromThisMealList.calories}"/>
            </td>
            <td><a href="<c:url value="/meals?action=update&id=${mealFromThisMealList.id}" /> ">edit</a></td>
            <td><a href="<c:url value="/meals?action=delete&id=${mealFromThisMealList.id}" /> ">delete</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>

<script>
    var buttn = document.getElementById("buttn");
    buttn.onclick = function () {
        buttn.value = (buttn.value.toString() == "Cancel") ? "Add Meal" : "Cancel";
        document.getElementById('addform').style.display = (buttn.value.toString() == "Cancel") ? "block" : "none";
        if (buttn.value.toString() == "Add Meal")
            window.location = "meals";
    };
    window.onload = function () {
        buttn.value = ((${empty mealFromServlet.id})) ? "Add Meal" : "Cancel";
        document.getElementById('addform').style.display = ((${empty mealFromServlet.id})) ? "none" : "block";
    };
</script>
