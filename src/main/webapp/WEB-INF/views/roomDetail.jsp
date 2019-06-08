<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Room info</title>
    <link href="../../resources/buildingDetailStyle.css" type="text/css" rel="stylesheet" />
</head>
<jsp:include page="../views/header.jsp" />
<body>
<h1>${error != null ? error : "Information about room:"}</h1>
<form action="/rooms/{id}" method="get">
    <div class="wrapper1">
        <div class="label">Id</div>
        <div class="label">Number</div>
        <div class="label">Building</div>
    </div>
    <div class="wrapper2">
        <div class="text">${Room.id}</div>
        <div class="text">${Room.number}</div>
        <div class="text">${Room.building}</div>

    </div>
</form>

<security:authorize access="hasRole('ROLE_ADMIN')">
<form action = "/rooms/${Room.id}" method = "post" >
    <div class="updateWr1" >
        <h2 > Update room</h2>
    </div >
    <div class="updateWr2" >        
        <div class="label" > Number </div >        
        <div class="label" > Building </div >
    </div >
    <div class="updateWr3" >        
        <div class="space" ><input type = "number" name = "number" min="1" max="1000" required value="${Room.number}" ></div >
        <div class="space" >
            <select name="building">
                <c:forEach items="${Buildings}" var="building" >
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
<div class="goBack" >
    <button class="btn" onclick="location.href='/'">Menu</button>
</div >

</body>
<jsp:include page="../views/footer.jsp" />
</html>
