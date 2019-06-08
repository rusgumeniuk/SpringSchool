<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Teachers</title>
    <link href="../../resources/teacherAllstyle.css" type="text/css" rel="stylesheet" />
</head>
<jsp:include page="../views/header.jsp" />
<body>
<h1>${result != null ? result : ""}</h1>
<form action="/teachers" method="get">
    <div class="wrapper1">
        <div class="label">Id</div>
        <div class="label">Full Name</div>
        <div class="label">Teacher rank</div>
    </div>
    <div>
        <c:forEach  items="${TeacherList}" var ="teacher">
            <div class="wrapper2">
                <div class="text">${teacher.id}</div>
                <div class="text">${teacher.fullName}</div>
                <div class="text">${teacher.teacherRank}</div>
                <div class="text">
                    <a type="button", class="btnAction", href="/teachers/${teacher.id}">Detail</a>
                    <security:authorize access="hasRole('ROLE_ADMIN')">
                        <a type="button", class="btnAction", href="/teachers/delete/${teacher.id}">Delete</a>
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
    <form action = "/teachers" method = "post" >
        <div class="createWr1" >
            <h2> Add new teacher</h2>
        </div>
        <div class="createWr2" >
            <div class="label" > Full name </div >
            <div class="label" > Cathedra </div >
            <div class="label" > Teacher rank </div >
            <div class="label" > Mentored group </div >
        </div >
        <div class="createWr3" >
            <div class="space" ><input type = "text" name = "fullName" required ></div >
            <div class="space" ><input type = "text" name = "cathedra" required ></div >
            <div class="space">
                <select required name="teacherRank">
                    <c:forEach items="${Ranks}" var="rank" >
                        <option value="${rank}">${rank}</option>
                    </c:forEach>
                </select>
            </div >
            <div class="space" >
                <select name="mentored_group" >
                    <c:forEach items="${Groups}" var="group">
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
</body>
<jsp:include page="../views/footer.jsp" />
</html>
