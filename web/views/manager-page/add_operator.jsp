<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 11.12.2019
  Time: 13:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Add operator</title>
</head>

<body>
<div>
    <h1>Add operator</h1>
</div>

<div>
    <div>
        <h2><c:out value="${add_operator_error}" /></h2>
    </div>

    <div>
        <h2>Please fill all fields<br />
            (in case of success you will be redirected to your menu page)</h2>
    </div>

    <form method="post">
        <table>
            <tbody>
            <tr>
                <td>First name:</td>
                <td>
                    <label>
                        <input type="text" name="first_name" style="text-transform:uppercase" pattern="^[A-Za-z -]{1,50}$" placeholder="IVAN" maxlength="50" required><br />
                    </label>
                </td>
            </tr>
            <tr>
                <td>Last name:</td>
                <td>
                    <label>
                        <input type="text" name="last_name" style="text-transform:uppercase" pattern="^[A-Za-z -]{1,50}$" placeholder="IVANOU-SIAMIONAU" maxlength="50" required><br />
                    </label>
                </td>
            </tr>
            <tr>
                <td>Position:</td>
                <td>
                    <label>
                        <input type="text" name="position" value="Operator" readonly><br />
                    </label>
                </td>
            </tr>
            <tr>
                <td>City:</td>
                <td>
                    <label>
                        <input type="text" name="city" value="${city}" readonly><br />
                    </label>
                </td>
            </tr>
            <tr>
                <td>E-mail:</td>
                <td>
                    <label>
                        <input type="email" name="email" placeholder="address@domain.com" maxlength="255" required><br />
                    </label>
                </td>
            </tr>
            <tr>
                <td>Phone number:</td>
                <td>
                    <label>
                        <input type="text" name="phone_number" pattern="^\+[0-9]{11,50}$" placeholder="+375291112233" maxlength="50" required><br />
                    </label>
                </td>
            </tr>
            <tr>
                <td>Password (minimum 8 characters):</td>
                <td>
                    <label>
                        <input type="password" name="password" id="password" pattern="[A-Za-z0-9]{8,255}" placeholder="digits and latin letters" maxlength="255" required><br />
                    </label>
                </td>
            </tr>
            <tr>
                <td>Confirm password:</td>
                <td>
                    <label for="password">
                        <input type="password" placeholder="digits and latin letters" maxlength="255" required
                               onfocus="confirm(document.getElementById('password'), this);"
                               oninput="confirm(document.getElementById('password'), this);"><br />
                    </label>
                </td>
            </tr>
            </tbody>
        </table>

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
    <button onclick="location.href='/manager-page'">Back to previous page</button><br />
</div>

<div>
    <button onclick="location.href='/'">Back to main</button>
</div>
</body>
</html>