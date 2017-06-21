<%-- 
    Document   : confirmation
    Created on : Jun 8, 2017, 7:31:39 PM
    Author     : Dragos
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>MakeMyDay</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="resources/css/main.css" type="text/css" rel="stylesheet">
        <style>
            *{
                color: rgb(95, 84, 73);
                margin: 0px;
                padding:0px;
                font-family: 'Ubuntu', sans-serif;
            }
            body{
                text-align: center;
                background-color: rgb(255, 149, 5);
            }
            h1{
                margin-top:20%;
            }
        </style>
    </head>
    <body>
        <h1>
            <c:out value="${message}"></c:out>
        </h1>
    </body>
</html>
