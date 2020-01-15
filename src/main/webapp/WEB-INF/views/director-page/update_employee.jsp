<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 22.12.2019
  Time: 12:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="app.entities.enums.AdminPosition" %>

<html>
<head>
    <title>Update employee</title>
</head>

<body>
<div>
    <h1>Update employee</h1>
</div>

<div>
    <div>
        <h2><c:out value="${update_employee_error}" /></h2>
    </div>

    <div>
        <h2>Please fill required fields<br />
            (in case of successful update you will be redirected to your menu page)</h2>
    </div>

    <form method="post">
        <table>
            <tbody>
            <tr>
                <td>First name:</td>
                <td>
                    <label>
                        <input type="text" name="first_name" id="first_name" style="text-transform:uppercase" pattern="^[A-Za-z -]{1,50}$" maxlength="50" onfocus="this.value=''" onblur="this.value=input('first_name', '${employee.getFirstName()}')" value="${employee.getFirstName()}" required><br />
                    </label>
                </td>
            </tr>
            <tr>
                <td>Last name:</td>
                <td>
                    <label>
                        <input type="text" name="last_name" id="last_name" style="text-transform:uppercase" pattern="^[A-Za-z -]{1,50}$" maxlength="50" onfocus="this.value=''" onblur="this.value=input('last_name', '${employee.getLastName()}')" value="${employee.getLastName()}" required><br />
                    </label>
                </td>
            </tr>
            <tr>
                <td>Position:</td>
                <td>
                    <c:if test="${employee_to_update_id == director_id}">
                    <label>
                        <input type="text" name="position" value="Director" readonly><br />
                    </label>
                    </c:if>
                    <c:if test="${employee_to_update_id != director_id}">
                    <label>
                        <select class="selectpicker" name="position">
                            <option <c:if test="${employee.getPosition().equals(AdminPosition.OPERATOR)}"> selected </c:if> value="${AdminPosition.OPERATOR}">${AdminPosition.OPERATOR.getTitle()}</option>
                            <option <c:if test="${employee.getPosition().equals(AdminPosition.MANAGER)}"> selected </c:if> value="${AdminPosition.MANAGER}">${AdminPosition.MANAGER.getTitle()}</option>
                            <option <c:if test="${employee.getPosition().equals(AdminPosition.DIRECTOR)}"> selected </c:if> value="${AdminPosition.DIRECTOR}">${AdminPosition.DIRECTOR.getTitle()}</option>
                        </select><br />
                    </label>
                    </c:if>
                </td>
            </tr>
            <tr>
                <td>City:</td>
                <td>
                    <%@page import="app.entities.enums.City"%>
                    <label>
                        <select class="selectpicker" name="city">
                            <option <c:if test="${employee.getCity().equals(City.MINSK)}"> selected </c:if> value="${City.MINSK}">${City.MINSK.getTitle()}</option>
                            <option <c:if test="${employee.getCity().equals(City.GOMEL)}"> selected </c:if> value="${City.GOMEL}">${City.GOMEL.getTitle()}</option>
                            <option <c:if test="${employee.getCity().equals(City.MOGILEV)}"> selected </c:if> value="${City.MOGILEV}">${City.MOGILEV.getTitle()}</option>
                            <option <c:if test="${employee.getCity().equals(City.BREST)}"> selected </c:if> value="${City.BREST}">${City.BREST.getTitle()}</option>
                            <option <c:if test="${employee.getCity().equals(City.GRODNO)}"> selected </c:if> value="${City.GRODNO}">${City.GRODNO.getTitle()}</option>
                        </select><br />
                    </label>
                </td>
            </tr>
            <tr>
                <td>E-mail:</td>
                <td>
                    <label>
                        <input type="email" name="email" id="email" maxlength="255" onfocus="this.value=''" onblur="this.value=input('email', '${employee.getEmail()}')" value="${employee.getEmail()}" required><br />
                    </label>
                </td>
            </tr>
            <tr>
                <td>Phone number:</td>
                <td>
                    <label>
                        <input type="text" name="phone_number" id="phone_number" pattern="^\+[0-9]{11,50}$" maxlength="50" onfocus="this.value=''" onblur="this.value=input('phone_number', '${employee.getPhoneNumber()}')" value="${employee.getPhoneNumber()}" required><br />
                    </label>
                </td>
            </tr>
            </tbody>
        </table>

        <script>
            function input(id, initial_pattern) {
                var input = document.getElementById(id).value;
                if (input !== initial_pattern && input !== '') {
                    return input;
                } else {
                    return initial_pattern;
                }
            }
        </script>

        <button type="submit">Submit</button>
    </form>
</div>

<div>
    <button onclick="location.href='/director-page'">Back to previous page</button><br />
</div>

<div>
    <button onclick="location.href='../../..'">Back to main</button>
</div>
</body>
</html>
