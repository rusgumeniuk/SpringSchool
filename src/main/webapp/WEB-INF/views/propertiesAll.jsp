<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Messages</title>
    <link href="../../resources/subjectAllstyle.css" type="text/css" rel="stylesheet" />
</head>
<jsp:include page="../views/header.jsp" />
<body>
<security:authorize access="hasRole('ROLE_ADMIN')">
    <h3>${result != null ? result : ""}</h3>
    <form action="/allProperties" method="get">
        <table border="1" cols>
            <caption>Properties from all modules:</caption>
            <tr>
                <td><b>School service's properties:</b></td>
                <td>${school}</td>
            </tr>
            <tr>
                <td><b>Lesson service's properties:</b></td>
                <td>${lesson}</td>
            </tr>
            <tr>
                <td><b>Server's properties:</b></td>
                <td>${server}</td>
            </tr>
            <tr>
                <td><b>Client's properties:</b></td>
                <td>${client}</td>
            </tr>
        </table>
    </form>
    <form action="/updateAllProperties" method="post">
        <h2>Update all properties from all modules</h2>
        <button type="submit">Do bus-refresh</button>
    </form>
    <div class="forSubmit" align="center">
        <button class="btn" onclick="location.href='../..'">Index</button>
    </div>
</security:authorize>
</body>
<jsp:include page="../views/footer.jsp" />
</html>