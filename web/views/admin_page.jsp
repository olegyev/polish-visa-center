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
    <title>Admin page</title>
</head>

<body>
    <h1>Welcome, dear Admin <c:out value="${admin_data.getFirstName()}" />!</h1>

    <div>
        <button onclick="location.href='/logout'">Logout</button><br />
    </div>
</body>
</html>