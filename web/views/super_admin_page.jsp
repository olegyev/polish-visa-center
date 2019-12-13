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
    <title>Super admin</title>

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
    <h1>Welcome, dear Super Admin <c:out value="${admin_data.getFirstName()}" />!</h1>

    <h2>Search admin</h2>
    <form method="post" action="super-admin-page?action=find_admin">
        <table>
            <tr>
                <td>
                    <label>
                        <input type="text" name="search_key" placeholder="id, email or last name">
                    </label>
                </td>
                <td>
                    <button type="submit">Find admin</button>
                </td>
            </tr>
        </table>
    </form>

    <form method="post" action="super-admin-page?action=show_all_admins">
        <button type="submit">Show all admins</button>
    </form>

    <div>
        <c:choose>
            <c:when test="${!empty admins}">
                <table border=1>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Position</th>
                        <th>Email</th>
                        <th>Phone number</th>
                        <th colspan=2>Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${admins}" var="admin">
                        <tr>
                            <td><c:out value="${admin.getId()}" /></td>
                            <td><c:out value="${admin.getFirstName()}" /></td>
                            <td><c:out value="${admin.getLastName()}" /></td>
                            <td><c:out value="${admin.getPosition().getTitle()}" /></td>
                            <td><c:out value="${admin.getEmail()}" /></td>
                            <td><c:out value="${admin.getPhoneNumber()}" /></td>
                            <td><a href="update-admin?adminId=<c:out value="${admin.getId()}" />">Update</a></td>
                            <td><a href="delete-admin?adminId=<c:out value="${admin.getId()}" />">Delete</a></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:when test="${empty admins}">
                <h3><c:out value="${no_matches}" /></h3>
            </c:when>
        </c:choose>

        <p><a class="hand" onclick="location.href='/add-admin'">Add admin</a></p>
    </div>

    <div>
        <button onclick="location.href='/logout'">Logout</button><br />
    </div>

    <div>
        <button onclick="location.href='/'">Back to main</button>
    </div>
</body>
</html>