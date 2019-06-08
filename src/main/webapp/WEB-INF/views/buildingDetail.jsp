<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Building info</title>
    <link href="../../resources/buildingDetailStyle.css" type="text/css" rel="stylesheet" />
</head>
<jsp:include page="../views/header.jsp" />
<body>
<form action="/buildings/{id}" method="get">
    <div class="wrapper1">
        <div class="label">Id</div>
        <div class="label">Number </div>
        <div class="label">Count of storeys</div>
    </div>
    <div class="wrapper2">
        <div class="text">${Building.id}</div>
        <div class="text">${building.number}</div>
        <div class="text">${building.countOfStoreys}</div>
    </div>
    <div class="wrapper1">
        <div align="center" class="label">Rooms of this building:</div>
    </div>
    <div class="wrapper1">
        <div class="label">Id</div>
        <div class="label">Number</div>
    </div>
    <c:forEach items="${Building.rooms}" var="room">
        <div class="wrapper2">
            <div class="text">${room.id}</div>
            <div class="text">${room.number}</div>
        </div>
    </c:forEach>
</form>

<security:authorize access="hasRole('ROLE_ADMIN')">
<form action = "/buildings/${Building.id}" method = "post" >
    <div class="updateWr1" >
        <h2 > Update building</h2>
    </div >
    <div class="updateWr4" >
        <div class="label">Number </div>
        <div class="label">Count of storeys</div>
    </div >
    <div class="updateWr5" >
        <div class="space" ><input type = "number" min="1" max="1000" name = "number" required ></div >
        <div class="space" ><input type = "number" min="100" max="1000" name = "countOfStoreys" required ></div >
    </div >

    <div class="forSubmit" >
        <button type = "submit" class="btn" > Submit </button >
    </div >
</form>
</security:authorize>
<div class="goBack" >
    <button class="btn" onclick="location.href='/'">Menu</button>
</div >
</body>
<jsp:include page="../views/footer.jsp" />
</html>
