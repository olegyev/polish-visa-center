<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 29.12.2019
  Time: 12:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Client</title>

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
<h1>Welcome, dear Client <c:out value="${client_data.getFirstName()}" />!</h1>

<div>
    <c:choose>
        <c:when test="${appointment != null}">
            <h2>Your appointment:</h2>
            <table border=1>
                <thead>
                <tr>
                    <th>City</th>
                    <th>Date</th>
                    <th>Time</th>
                    <th colspan=2>Action</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td><c:out value="${appointment.getCity().getTitle()}" /></td>
                    <td><c:out value="${appointment.getAppointmentDate().toString()}" /></td>
                    <td><c:out value="${appointment.getAppointmentTime()}" /></td>
                    <form method="post" id="update" action="client-page?action=update&appointmentId=<c:out value="${appointment.getId()}" />">
                        <td><a href="javascript:" onclick="document.getElementById('update').submit();">Update</a></td>
                    </form>
                    <form method="post" id="delete" action="client-page?action=delete&appointmentId=<c:out value="${appointment.getId()}" />">
                        <td><a href="javascript:" onclick="askIfDelete()">Delete</a></td>
                        <input type="hidden" name="mess" value="Delete"/>

                        <script>
                            function askIfDelete() {
                                var answer = window.confirm("Are you sure you want to delete your appointment "
                                                            + "in <c:out value="${appointment.getCity().getTitle()}" /> "
                                                            + "on <c:out value="${appointment.getAppointmentDate().toString()}" /> "
                                                            + "at <c:out value="${appointment.getAppointmentTime()}" />?");
                                if (answer) {
                                    var confirm = document.getElementById('delete').submit();
                                    confirm.submit();
                                } else {
                                    return false;
                                }
                            }
                        </script>
                    </form>
                </tr>
                </tbody>
            </table><br />
        </c:when>
        <c:when test="${appointment == null}">
            <h2>Here will be visa status...</h2>
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