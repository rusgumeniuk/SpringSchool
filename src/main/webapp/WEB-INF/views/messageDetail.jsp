<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Group info</title>
    <link href="../../resources/groupDetailStyle.css" type="text/css" rel="stylesheet" />
</head>
<jsp:include page="../views/header.jsp" />
<body>
<security:authorize access="hasRole('ROLE_ADMIN')">
    <form action="/messages/{id}" method="get">
        <table border="0">
            <tr>
                <td>Id</td>
                <td>${Message.id}</td>
            </tr>
            <tr>
                <td>Class name</td>
                <td>${Message.className}</td>
            </tr>
            <tr>
                <td>Status code</td>
                <td>${Message.statusCode}</td>
            </tr>
            <tr>
                <td>Http method</td>
                <td>${Message.httpMethod}</td>
            </tr>
            <tr>
                <td>Timestamp</td>
                <td>${Message.dateTime}</td>
            </tr>
            <tr>
                <td>Error</td>
                <td>${Message.error}</td>
            </tr>
            <tr>
                <td>Description</td>
                <td>${Message.description}</td>
            </tr>
        </table>
    </form>

    <div class="goBack" >
        <button class="btn" onclick="location.href='/menu'">Menu</button>
    </div >
    </body>
</security:authorize>
<jsp:include page="../views/footer.jsp" />
</html>
