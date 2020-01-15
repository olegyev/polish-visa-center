<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 11.12.2019
  Time: 13:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Manager</title>

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
    <h1>Welcome, dear Manager <c:out value="${admin_data.getFirstName()}" />!</h1>

    <h2>Search operator</h2>
    <form method="post" action="manager-page?action=find_operator">
        <table>
            <tr>
                <td>
                    <label>
                        <input type="text" name="search_key" placeholder="id, email or last name" maxlength="255">
                    </label>
                </td>
                <td>
                    <button type="submit">Find operator</button>
                </td>
            </tr>
        </table>
    </form>

    <form method="post" action="manager-page?action=show_all_operators">
        <button type="submit">Show all operators</button>
    </form>

    <div>
        <c:choose>
            <c:when test="${!empty operators}">
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
                    <c:forEach items="${operators}" var="operator">
                        <tr>
                            <td><c:out value="${operator.getId()}" /></td>
                            <td><c:out value="${operator.getFirstName()}" /></td>
                            <td><c:out value="${operator.getLastName()}" /></td>
                            <td><c:out value="${operator.getPosition().getTitle()}" /></td>
                            <td><c:out value="${operator.getCity().getTitle()}" /></td>
                            <td><c:out value="${operator.getEmail()}" /></td>
                            <td><c:out value="${operator.getPhoneNumber()}" /></td>
                            <td><a href="manager-page/update-operator?operatorId=<c:out value="${operator.getId()}" />">Update</a></td>
                            <td><a href="manager-page/delete-operator?operatorId=<c:out value="${operator.getId()}" />">Delete</a></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:when test="${empty operators}">
                <h3><c:out value="${no_matches}" /></h3>
            </c:when>
        </c:choose>

        <p><a class="hand" onclick="location.href='/manager-page/add-operator'">Add operator</a></p>
    </div>

    <div>
        <button onclick="location.href='/logout'">Logout</button><br />
    </div>

    <div>
        <button onclick="location.href='..'">Back to main</button>
    </div>
</body>
</html>