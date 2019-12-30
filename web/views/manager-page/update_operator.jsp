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
    <title>Update operator</title>
</head>

<body>
<div>
    <h1>Update operator</h1>
</div>

<div>
    <div>
        <h2><c:out value="${update_operator_error}" /></h2>
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
                        <input type="text" name="first_name" id="first_name" style="text-transform:uppercase" pattern="^[A-Za-z -]{1,50}$" maxlength="50" onfocus="this.value=''" onblur="this.value=input('first_name', '${operator.getFirstName()}')" value="${operator.getFirstName()}" required><br />
                    </label>
                </td>
            </tr>
            <tr>
                <td>Last name:</td>
                <td>
                    <label>
                        <input type="text" name="last_name" id="last_name" style="text-transform:uppercase" pattern="^[A-Za-z -]{1,50}$" maxlength="50" onfocus="this.value=''" onblur="this.value=input('last_name', '${operator.getLastName()}')" value="${operator.getLastName()}" required><br />
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
                        <input type="email" name="email" id="email" maxlength="255" onfocus="this.value=''" onblur="this.value=input('email', '${operator.getEmail()}')" value="${operator.getEmail()}" required><br />
                    </label>
                </td>
            </tr>
            <tr>
                <td>Phone number:</td>
                <td>
                    <label>
                        <input type="text" name="phone_number" id="phone_number" pattern="^\+[0-9]{11,50}$" maxlength="50" onfocus="this.value=''" onblur="this.value=input('phone_number', '${operator.getPhoneNumber()}')" value="${operator.getPhoneNumber()}" required><br />
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