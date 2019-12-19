<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 11.12.2019
  Time: 17:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Delete admin</title>

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
        <c:if test="${admin_to_delete_id == this_admin_id}">
        <h2><c:out value="${delete_admin_error}" /></h2>
        <button onclick="location.href='/super-admin-page'">Ok</button>
        </c:if>
    </div>

    <div style="text-align: center">
        <c:if test="${admin_to_delete_id != this_admin_id}">
        <h2>Are you sure you want to delete admin <c:out value="${admin_to_delete_first_name}" /> <c:out value="${admin_to_delete_last_name}" /> with ID = <c:out value="${admin_to_delete_id}" />?</h2>
            <table>
                <tr>
                    <td>
                        <form method="post">
                            <button type="submit">Yes</button>
                        </form>
                    </td>
                    <td valign="top">
                        <button onclick="location.href='/super-admin-page'">No</button>
                    </td>
                </tr>
            </table>
        </c:if>
    </div>
</body>
</html>