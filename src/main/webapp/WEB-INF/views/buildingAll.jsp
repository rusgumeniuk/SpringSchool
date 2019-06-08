<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Buildings</title>
    <link href="../../resources/roomAllstyle.css" type="text/css" rel="stylesheet" />
</head>
<jsp:include page="../views/header.jsp" />
<body>
<form action="/buildings" method="get">
    <div class="wrapper1">
        <div class="label">Id</div>
        <div class="label">Number</div>
        <div class="label">Count of storeys</div>
    </div>
    <div>
        <c:forEach  items="${BuildingList}" var ="building">
            <div class="wrapper2">
                <div class="text">${building.id}</div>
                <div class="text">${building.number}</div>
                <div class="text">${building.countOfStoreys}</div>
                <div class="text">
                    <a type="button", class="btnAction", href="/buildings/${building.id}">Detail</a>
                    <security:authorize access="hasRole('ROLE_ADMIN')">
                    <a type="button", class="btnAction", href="/buildings/delete/${building.id}">Delete</a>
                    </security:authorize>
                </div>
            </div>
        </c:forEach>
    </div>
</form>

<div class="goBack" >
    <button class="btn" onclick="location.href='/'">Menu</button>
</div >
<security:authorize access="hasRole('ROLE_ADMIN')">
<form action = "/buildings" method = "post" >
    <div class="createWr1" >
        <h2> Add new building</h2>
    </div>
    <div class="createWr4" >
        <div class="label">Number </div>
        <div class="label">Count of storeys</div>
    </div >
    <div class="createWr5" >
        <div class="space" ><input type = "number" min="1" max="1000" name = "number" required ></div >
        <div class="space" ><input type = "number" min="100" max="1000" name = "countOfStoreys" required ></div >
    </div >

    <div class="forSubmit" >
        <button type = "submit" class="btn" > Submit </button >
    </div >
</form>
</security:authorize>
</body>
<jsp:include page="../views/footer.jsp" />
</html>
