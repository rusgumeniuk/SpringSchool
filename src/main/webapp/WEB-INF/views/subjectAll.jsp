<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Subjects</title>
    <link href="../../resources/subjectAllstyle.css" type="text/css" rel="stylesheet" />
</head>
<jsp:include page="../views/header.jsp" />
<body>
<form action="/subject" method="get">
    <div class="wrapper1">
        <div class="label">Id</div>
        <div class="label">Title</div>
    </div>
    <div>
        <c:forEach  items="${subList}" var ="subject">
            <div class="wrapper2">
                <div class="text">${subject.id}</div>
                <div class="text">${subject.title}</div>
                <div class="text">
                    <a type="button", class="btnAction", href="/subject/${subject.id}">Detail</a>
                    <a type="button", class="btnAction", href="/subject/delete/${subject.id}">Delete</a>
                </div>
            </div>
        </c:forEach>
    </div>
</form>

<div class="forSubmit"  align="center" >
    <button class="btn"  onclick="location.href='/menu'">Menu</button>
</div >

<form action = "/subject/create" method = "post" >
    <div class="createWr1" >
        <h2> Add new subject</h2>
    </div>
    <div class="createWr2" >
        <div class="label" > Title </div >
    </div >
    <div class="createWr3" >
        <div class="space" ><input type = "text" name = "title" required ></div >
    </div >

    <div class="forSubmit" >
        <button type = "submit" class="btn" > Submit </button >
    </div >
</form>
</body>
<jsp:include page="../views/footer.jsp" />
</html>
