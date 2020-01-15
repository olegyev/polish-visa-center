<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 02.12.2019
  Time: 17:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Login</title>

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
<div>
    <h1>Poland Visa Center</h1>
</div>

<div>
    <div>
        <h2><c:out value="${login_error}" /></h2>
    </div>

    <div>
        <h2>Please login or register</h2>
    </div>

    <form method="post">
        <table>
            <tbody>
            <tr>
                <td>E-Mail:</td>
                <td>
                    <label>
                        <input type="email" name="email" placeholder="address@domain.com" maxlength="255" required><br />
                    </label>
                </td>
            </tr>
            <tr>
                <td>Password:</td>
                <td>
                    <label>
                        <input type="password" name="password" maxlength="255" required><br />
                    </label>
                </td>
            </tr>
            <tr>
                <td>Login as admin:</td>
                <td>
                    <label>
                        <input type="checkbox" name="admin_login"><br />
                    </label>
                </td>
            </tr>
            </tbody>
        </table>

        <button type="submit">Login</button>
        &nbsp;
        <a class="hand" onclick="location.href='/registration'">Register</a>
    </form>
</div>

<div>
    <p><button onclick="location.href='..'">Back to main</button></p>
</div>
</body>
</html>