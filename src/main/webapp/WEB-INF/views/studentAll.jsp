<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Students</title>
    <link href="../../resources/studentsAllstyle.css" type="text/css" rel="stylesheet" />
</head>
<body>
<form action="/students" method="get">
    <div class="wrapper1">
        <div class="label">Id</div>
        <div class="label">Name</div>
        <div class="label">Group</div>
    </div>
    <div>
        <c:forEach  items="${StudentList}" var ="student">
            <div class="wrapper2">
                <div class="text">${student.id}</div>
                <div class="text">${student.name}</div>
                <div class="text">${student.group}</div>
                <div class="text">
                    <a type="button", class="btnAction", href="/students/${student.id}">Detail</a>
                    <a type="button", class="btnAction", href="/students/delete/${student.id}">Delete</a>
                </div>
            </div>
        </c:forEach>
    </div>
</form>

<div class="goBack" >
    <button class="btn" onclick="location.href='/menu'">Menu</button>
</div >

<form action = "/students/create" method = "post" >
    <div class="createWr1" >
        <h2> Add new student</h2>
    </div>
    <div class="createWr2" >
        <div class="label" > Name </div >
        <div class="label" > Group </div >
    </div >
    <div class="createWr3" >
        <div class="space" ><input type = "text" name = "name" required ></div >
        <div class="space" >
            <select name="group" >
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
</body>
</html>
