<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Teacher info</title>
    <link href="../../resources/groupDetailStyle.css" type="text/css" rel="stylesheet" />
</head>
<jsp:include page="../views/header.jsp" />
<body>
<h1>${error != null ? error : "Information about teacher:"}</h1>
<form action="/teachers/{id}" method="get">
    <div class="wrapper1">
        <div class="label">Id</div>
        <div class="label">Full Name</div>
        <div class="label">Teacher rank</div>
        <div class="label">Cathedra</div>
        <div class="label">Mentored Group</div>
    </div>
    <div class="wrapper2">
        <div class="text">${Teacher.id}</div>
        <div class="text">${Teacher.fullName}</div>
        <div class="text">${Teacher.teacherRank}</div>
        <div class="text">${Teacher.cathedra}</div>
        <div class="text">${Teacher.mentored_group}</div>

    </div>
</form>

<security:authorize access="hasRole('ROLE_ADMIN')">
<form action = "/teachers/${Teacher.id}" method = "post" >
    <div class="updateWr1" >
        <h2 > Update teacher</h2>
    </div >
    <div class="updateWr2" >
        <div class="label">Full Name</div>
        <div class="label">Cathedra</div>
        <div class="label">Teacher rank</div>
        <div class="label">Mentored Group</div>
    </div >
    <div class="updateWr3" >
        <div class="space" ><input type = "text" name = "fullName" required value="${Teacher.fullName}" ></div >
        <div class="space" ><input type = "text" name = "cathedra" required value="${Teacher.cathedra}" ></div >
        <div class="space" >
            <select required name="teacherRank">
                <c:forEach items="${Ranks}" var="rank" >
                    <option value="${rank}">${rank}</option>
                </c:forEach>
            </select>
        </div >
        <div  class="space" >
            <select name="mentored_group">
                <c:forEach items="${Groups}" var="group" >
                    <option value="${group.id}">${group}</option>
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
