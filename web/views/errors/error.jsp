<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 19.12.2019
  Time: 15:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Error Handling</title>
</head>

<body>
    <h2>An error occurred!</h2>
    <p>Code: <c:out value="${error_code}" /></p>
    <p>Source of error: <c:out value="${error_uri}" /></p>

    <div>
        <button onclick="location.href='/'">Back to main</button>
    </div>
</body>
</html>