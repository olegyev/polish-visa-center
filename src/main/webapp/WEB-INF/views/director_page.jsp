<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 22.12.2019
  Time: 11:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Director</title>

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
    <h1>Welcome, dear Director <c:out value="${admin_data.getFirstName()}" />!</h1>

    <h2>Search employee</h2>
    <form method="post" action="director-page?action=find_employee">
        <table>
            <tr>
                <td>
                    <label>
                        <input type="text" name="search_key" placeholder="id, email, last name or city" maxlength="255">
                    </label>
                </td>
                <td>
                    <button type="submit">Find employee</button>
                </td>
            </tr>
        </table>
    </form>

    <form method="post" action="director-page?action=show_all_employees">
        <button type="submit">Show all employees</button>
    </form>

    <div>
        <c:choose>
            <c:when test="${!empty employees}">
                <table border=1>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Position</th>
                        <th>City</th>
                        <th>Email</th>
                        <th>Phone number</th>
                        <th colspan=2>Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${employees}" var="employee">
                        <tr>
                            <td><c:out value="${employee.getId()}" /></td>
                            <td><c:out value="${employee.getFirstName()}" /></td>
                            <td><c:out value="${employee.getLastName()}" /></td>
                            <td><c:out value="${employee.getPosition().getTitle()}" /></td>
                            <td><c:out value="${employee.getCity().getTitle()}" /></td>
                            <td><c:out value="${employee.getEmail()}" /></td>
                            <td><c:out value="${employee.getPhoneNumber()}" /></td>
                            <td><a href="director-page/update-employee?employeeId=<c:out value="${employee.getId()}" />">Update</a></td>
                            <td><a href="director-page/delete-employee?employeeId=<c:out value="${employee.getId()}" />">Delete</a></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:when test="${empty employees}">
                <h3><c:out value="${no_matches}" /></h3>
            </c:when>
        </c:choose>

        <p><a class="hand" onclick="location.href='/director-page/add-employee'">Add employee</a></p>
    </div>

    <div>
        <button onclick="location.href='/logout'">Logout</button><br />
    </div>

    <div>
        <button onclick="location.href='..'">Back to main</button>
    </div>
</body>
</html>