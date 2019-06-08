<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Rooms</title>
    <link href="../../resources/roomAllstyle.css" type="text/css" rel="stylesheet" />
</head>
<jsp:include page="../views/header.jsp" />
<body>
<h1>${result != null ? result : ""}</h1>
<form action="/rooms" method="get">
    <div class="wrapper1">
        <div class="label">Id</div>
        <div class="label">Number</div>
        <div class="label">Building</div>
    </div>
    <div>
        <c:forEach  items="${RoomList}" var ="room">
            <div class="wrapper2">
                <div class="text">${room.id}</div>
                <div class="text">${room.number}</div>
                <div class="text">${room.building}</div>
                <div class="text">
                    <a type="button", class="btnAction", href="/rooms/${room.id}">Detail</a>
                    <security:authorize access="hasRole('ROLE_ADMIN')">
                        <a type="button", class="btnAction", href="/rooms/delete/${room.id}">Delete</a>
                    </security:authorize>

                </div>
            </div>
        </c:forEach>
    </div>
</form>
<div class="goBack" >
    <button class="btn" onclick="location.href='/'">Menu</button>
</div>
<security:authorize access="hasRole('ROLE_ADMIN')">
    <form action = "/rooms" method = "post" name="object" >
        <div class="createWr1" >
            <h2> Add new room</h2>
        </div>
        <div class="createWr2" >            
            <div class="label" > Number </div >            
            <div class="label" > Building </div >
        </div >
        <div class="createWr3" >            
            <div class="space" ><input type = "number" min="1" max="1000" name = "number" required ></div >            
            <div class="space" >
                <select name="building" >
                    <c:forEach items="${Buildings}" var="building">
                        <option value="${building.id}">${building}</option>
                    </c:forEach>
                </select>
            </div >
        </div >

        <div class="forSubmit" >
            <button type = "submit" class="btn" > Submit </button >
        </div >
    </form>
</security:authorize>

</body>
<jsp:include page="../views/footer.jsp" />
</html>
