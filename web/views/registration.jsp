<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 02.12.2019
  Time: 17:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Registration</title>
    <style>
        p {
            line-height: 1em;
        }
    </style>
</head>

<body>
<div>
    <h1>Poland Visa Center</h1>
</div>

<div>
    <div>
        <h2><c:out value="${register_error}" /></h2>
    </div>

    <div>
        <h2>Please fill all fields as in passport<br />
        (in case of success you will be redirected to the login page)</h2>
    </div>

    <form method="post">
        <table>
            <tbody>
            <tr>
                <td>First name:</td>
                <td>
                    <label>
                        <input type="text" name="first_name" style="text-transform:uppercase" pattern="^[A-Za-z -]{1,50}$" placeholder="IVAN" required><br />
                    </label>
                </td>
            </tr>
            <tr>
                <td>Last name:</td>
                <td>
                    <label>
                        <input type="text" name="last_name" style="text-transform:uppercase" pattern="^[A-Za-z -]{1,50}$" placeholder="IVANOU-SIAMIONAU" required><br />
                    </label>
                </td>
            </tr>
            <tr>
                <td>Date of birth:</td>
                <td>
                    <label>
                        <input type="date" name="date_of_birth" id="date" min="1900-01-01" max="today" required><br />
                    </label>
                    <script>
                        var today = new Date();
                        var dd = today.getDate();
                        var mm = today.getMonth() + 1; // January is 0.
                        var yyyy = today.getFullYear();
                        if (dd < 10){
                            dd = '0' + dd;
                        }
                        if (mm < 10){
                            mm = '0' + mm;
                        }
                        today = yyyy + '-' + mm + '-' + dd;
                        document.getElementById("date").setAttribute("max", today);
                    </script>
                </td>
            </tr>
            <tr>
                <td>Occupation:</td>
                <td>
                    <%@page import="app.entities.enums.ClientOccupation"%>
                    <label>
                        <select class="selectpicker" name="occupation">
                            <c:forEach items="<%=ClientOccupation.values()%>" var="occupation">
                                <option value="${occupation}">${ClientOccupation.valueOf(occupation).getTitle()}</option>
                            </c:forEach>
                        </select><br />
                    </label>
                </td>
            </tr>
            <tr>
                <td>E-mail:</td>
                <td>
                    <label>
                        <input type="email" name="email" id="email" onfocus="this.value=''" onblur="this.value=input_email(this)" placeholder="address@domain.com" required><br />
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
                        <input type="text" name="phone_number" pattern="^\+[0-9]{11,50}$" placeholder="+375291112233" required><br />
                    </label>
                </td>
            </tr>
            <tr>
                <td>Password (minimum 8 characters):</td>
                <td>
                    <label>
                        <input type="password" name="password" id="password" pattern="[A-Za-z0-9]{8,255}" placeholder="digits and latin letters" required><br />
                    </label>
                </td>
            </tr>
            <tr>
                <td>Confirm password:</td>
                <td>
                    <label for="password">
                        <input type="password" placeholder="digits and latin letters" required
                               onfocus="confirm(document.getElementById('password'), this);"
                               oninput="confirm(document.getElementById('password'), this);"><br />
                    </label>
                </td>
            </tr>
            <tr>
                <td>I agree to the processing of personal data:</td>
                <td>
                    <label>
                        <input type="checkbox" name="pers_data_agree" required><br />
                    </label>
                </td>
            </tr>
            </tbody>
        </table>

        <script>
            var add_email = "";
            function input_email(input) {
                if (input.value !== '') {
                    this.add_email = input.value;
                }
                return this.add_email;
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
    <button onclick="location.href='/login'">Back to previous page</button><br />
</div>

<div>
    <button onclick="location.href='/'">Back to main</button>
</div>
</body>
</html>