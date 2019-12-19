<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 12.12.2019
  Time: 12:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Update admin</title>
</head>
<body>
<div>
    <h1>Update admin</h1>
</div>

<div>
    <div>
        <h2><c:out value="${update_admin_error}" /></h2>
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
                        <input type="text" name="first_name" id="first_name" style="text-transform:uppercase" pattern="^[A-Za-z -]{1,50}$" onfocus="this.value=''" onblur="this.value=input('first_name', '${admin.getFirstName()}')" value="${admin.getFirstName()}" required><br />
                    </label>
                </td>
            </tr>
            <tr>
                <td>Last name:</td>
                <td>
                    <label>
                        <input type="text" name="last_name" id="last_name" style="text-transform:uppercase" pattern="^[A-Za-z -]{1,50}$" onfocus="this.value=''" onblur="this.value=input('last_name', '${admin.getLastName()}')" value="${admin.getLastName()}" required><br />
                    </label>
                </td>
            </tr>
            <tr>
                <td>Position:</td>
                <td>
                    <%@page import="app.entities.enums.AdminPosition"%>
                    <label>
                        <select class="selectpicker" name="position">
                            <option <c:if test="${admin.getPosition().equals(AdminPosition.OPERATOR)}"> selected </c:if> value="${AdminPosition.OPERATOR}">${AdminPosition.OPERATOR.getTitle()}</option>>
                            <option <c:if test="${admin.getPosition().equals(AdminPosition.MANAGER)}"> selected </c:if> value="${AdminPosition.MANAGER}">${AdminPosition.MANAGER.getTitle()}</option>>
                        </select><br />
                    </label>
                </td>
            </tr>
            <tr>
                <td>E-mail:</td>
                <td>
                    <label>
                        <input type="email" name="email" id="email" onfocus="this.value=''" onblur="this.value=input('email', '${admin.getEmail()}')" value="${admin.getEmail()}" required><br />
                    </label>
                </td>
            </tr>
            <tr>
                <td>Confirm e-mail:</td>
                <td>
                    <label for="email">
                        <input type="email" placeholder="address@domain.com" required
                               onfocus="confirm(document.getElementById('email'), this);"
                               oninput="confirm(document.getElementById('email'), this);"><br />
                    </label>
                </td>
            </tr>
            <tr>
                <td>Phone number:</td>
                <td>
                    <label>
                        <input type="text" name="phone_number" id="phone_number" pattern="^\+[0-9]{11,50}$" onfocus="this.value=''" onblur="this.value=input('phone_number', '${admin.getPhoneNumber()}')" value="${admin.getPhoneNumber()}" required><br />
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

        <script>
            function confirm(elementOne, elementTwo) {
                if (elementOne.value !== elementTwo.value || elementOne.value === '' || elementTwo.value === '') {
                    elementTwo.setCustomValidity('Неверное значение поля.');
                } else {
                    elementTwo.setCustomValidity('');
                }
            }
        </script>

        <button type="submit">Submit</button>
    </form>
</div>

<div>
    <button onclick="location.href='/super-admin-page'">Back to previous page</button><br />
</div>

<div>
    <button onclick="location.href='../..'">Back to main</button>
</div>
</body>
</html>