<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 09.12.2019
  Time: 16:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Operator</title>

    <style type="text/css">
        A {
            color: blue;
        }
        A:link { text-decoration: none; }
        A:visited { text-decoration: none; }
        A:active { text-decoration: none; }
        A:hover {
            text-decoration: underline;
            color: red;
        }
        .hand { cursor: pointer; }
    </style>
</head>

<body>
    <h1>Welcome, dear Operator <c:out value="${admin_data.getFirstName()}" />!</h1>

    <h2>Search appointment</h2>
    <form method="post" action="operator-page?action=find_appointment">
        <table>
            <tr>
                <td>
                    <label>
                        <input type="text" name="search_key" placeholder="date in your city (yyyy-mm-dd) or client id, last name, email" size="47" maxlength="255">
                    </label>
                </td>
                <td>
                    <button type="submit">Find appointment</button>
                </td>
            </tr>
        </table>
    </form>

    <table>
        <tr>
            <td>
                <form method="post" action="operator-page?action=today">
                    <button type="submit">Today</button>
                </form>
            </td>
            <td>
                <form method="post" action="operator-page?action=show_all_appointments">
                    <button type="submit">Show all appointments</button>
                </form>
            </td>
        </tr>
    </table>

    <div>
        <c:choose>
            <c:when test="${!empty appointments}">
                <table border=1>
                    <thead>
                    <tr>
                        <th>City</th>
                        <th>Date</th>
                        <th>Time</th>
                        <th>Client ID</th>
                        <th>First name</th>
                        <th>Last name</th>
                        <th>E-mail</th>
                        <th>Phone number</th>
                        <th>Date of birth</th>
                        <th>Occupation</th>
                        <th>Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${appointments}" var="appointment">
                        <tr>
                            <td><c:out value="${appointment.getCity().getTitle()}" /></td>
                            <td><c:out value="${appointment.getAppointmentDate().toString()}" /></td>
                            <td><c:out value="${appointment.getAppointmentTime()}" /></td>
                            <td><c:out value="${appointment.getClient().getId()}" /></td>
                            <td><c:out value="${appointment.getClient().getFirstName()}" /></td>
                            <td><c:out value="${appointment.getClient().getLastName()}" /></td>
                            <td><c:out value="${appointment.getClient().getEmail()}" /></td>
                            <td><c:out value="${appointment.getClient().getPhoneNumber()}" /></td>
                            <td><c:out value="${appointment.getClient().getDateOfBirth()}" /></td>
                            <td><c:out value="${appointment.getClient().getOccupation().getTitle()}" /></td>
                            <c:choose>
                                <c:when test="${appointment.getCity().equals(operator.getCity())}">
                                    <td><a href="operator-page/set-visa-status?employeeId=<c:out value="${employee.getId()}" />">Set visa status</a></td>
                                </c:when>
                                <c:when test="${!appointment.getCity().equals(operator.getCity())}">
                                    <td>Denied</td>
                                </c:when>
                            </c:choose>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table><br />
            </c:when>
            <c:when test="${empty appointments}">
                <h3><c:out value="${no_matches}" /></h3><br />
            </c:when>
        </c:choose>
    </div>

    <div>
        <button onclick="location.href='/logout'">Logout</button><br />
    </div>

    <div>
        <button onclick="location.href='/'">Back to main</button>
    </div>
</body>
</html>