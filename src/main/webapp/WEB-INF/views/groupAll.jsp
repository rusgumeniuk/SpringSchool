<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Groups</title>
    <link href="../../resources/studentAllstyle.css" type="text/css" rel="stylesheet" />
</head>
<jsp:include page="../views/header.jsp" />
<body>
<form action="/groups" method="get">
    <div class="wrapper1">
        <div class="label">Id</div>
        <div class="label">Title</div>
    </div>
    <div>
        <c:forEach  items="${GroupList}" var ="group">
            <div class="wrapper2">
                <div class="text">${group.id}</div>
                <div class="text">${group.title}</div>
                <div class="text">
                    <a type="button", class="btnAction", href="/groups/${group.id}">Detail</a>
                    <a type="button", class="btnAction", href="/groups/${group.id}", methods="delete">Delete</a>
                </div>
            </div>
        </c:forEach>
    </div>
</form>

<div class="goBack" >
    <button class="btn" onclick="location.href='/menu'">Menu</button>
</div >

<form action = "/groups/create" method = "post" >
    <div class="createWr1" >
        <h2> Add new group</h2>
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
