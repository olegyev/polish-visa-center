<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 28.11.2019
  Time: 17:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Trip aims</title>

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
    <h2>Please specify the aim of the trip</h2>
    <p><a class="hand" onclick="location.href='/visa-types/c-visa'">Tourism/Other private visits</a></p>
    <p><a class="hand" onclick="location.href='/visa-types/c-visa'">Transit</a></p>
    <p><a class="hand" onclick="location.href='/visa-types/c-visa'">Business/Work</a></p>
    <p><a class="hand" onclick="location.href='/visa-types/c-visa'">Study or training events or activities</a></p>
    <p><a class="hand" onclick="location.href='/visa-types/c-visa'">Close Relatives/Family Members Visits</a></p>
    <p><a class="hand" onclick="location.href='/visa-types/c-visa'">Medical Treatment</a></p>
    <p><a class="hand" onclick="location.href='/visa-types/c-visa'">Official delegations</a></p>
    <p><a class="hand" onclick="location.href='/visa-types/c-visa'">Other travel aims</a></p>
    <p><a class="hand" onclick="location.href='/visa-types/d-visa'">National Visa-D</a></p>
</div>

<div>
    <button onclick="location.href='/'">Back to main</button>
</div>
</body>
</html>