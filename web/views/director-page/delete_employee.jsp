<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 22.12.2019
  Time: 12:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Delete employee</title>

    <style>
        table {
            width: 300px;
            margin: auto;
        }
        td {
            text-align: center;
        }
    </style>
</head>

<body>
    <div style="text-align: center">
        <c:if test="${employee_to_delete_id == director_id}">
            <h2><c:out value="${delete_employee_error}" /></h2>
            <button onclick="location.href='/director-page'">Ok</button>
        </c:if>
    </div>

    <div style="text-align: center">
        <c:if test="${employee_to_delete_id != director_id}">
            <h2>Are you sure you want to delete employee <c:out value="${employee_to_delete_first_name}" /> <c:out value="${employee_to_delete_last_name}" /> with ID = <c:out value="${employee_to_delete_id}" />?</h2>
                <table>
                    <tr>
                        <td>
                        <form method="post">
                            <button type="submit">Yes</button>
                        </form>
                    </td>
                    <td valign="top">
                        <button onclick="location.href='/director-page'">No</button>
                    </td>
                </tr>
            </table>
        </c:if>
    </div>
</body>
</html>