<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Group info</title>
    <link href="../../resources/groupDetailStyle.css" type="text/css" rel="stylesheet" />
</head>
<jsp:include page="../views/header.jsp" />
<body>
<form action="/groups/{id}" method="get">
    <div class="wrapper1">
        <div class="label">Id</div>
        <div class="label">Title</div>
        <div class="label">Cathedra</div>
        <div class="label">Start year</div>
        <div class="label">Mentor</div>
    </div>
    <div class="wrapper2">
        <div class="text">${Group.id}</div>
        <div class="text">${Group.title}</div>
        <div class="text">${Group.cathedra}</div>
        <div class="text">${Group.startYear}</div>
        <div class="text">${Group.mentor}</div>
    </div>
    <div class="wrapper1">
        <div align="center" class="label">Students of this group:</div>
    </div>
    <div class="wrapper1">
        <div class="label">Id</div>
        <div class="label">Name</div>
    </div>
    <c:forEach items="${Group.students}" var="student">
        <div class="wrapper2">
            <div class="text">${student.id}</div>
            <div class="text">${student.name}</div>
        </div>
    </c:forEach>
</form>


<form action = "/groups/${Group.id}" method = "post" >
    <div class="updateWr1" >
        <h2 > Update group</h2>
    </div >
    <div class="updateWr4" >
        <div class="label">Title</div>
        <div class="label">Cathedra</div>
        <div class="label">Start year</div>
        <div class="label">Mentor</div>
    </div >
    <div class="updateWr5" >
        <div class="space" ><input type = "text" name = "title" value="${Group.title}" required ></div >
        <div class="space" ><input type = "text" name = "cathedra" value="${Group.cathedra}" required ></div >
        <div class="space" ><input type = "number" min= "1970" max= "2018" name = "startYear" value="${Group.startYear}" required ></div >
        <div class="space" >
            <select name="mentor">
                <c:forEach items="${Mentors}" var="mentor" >
                    <option value="${mentor.id}">${mentor}</option>
                </c:forEach>
            </select>
        </div >
    </div >

    <div class="forSubmit" >
        <button type = "submit" class="btn" > Submit </button >
    </div >
</form>

<div class="goBack" >
    <button class="btn" onclick="location.href='/menu'">Menu</button>
</div >
</body>
<jsp:include page="../views/footer.jsp" />
</html>