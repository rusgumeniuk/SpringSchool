<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Subject</title>
    <link href="../../resources/subjectDetailStyle.css" type="text/css" rel="stylesheet" />
</head>
<jsp:include page="../views/header.jsp" />
<body>
<h1>${result != null ? result : ""}</h1>
<form action="/subjects/{id}" method="get">
    <div class="wrapper1">
        <div class="label">Id</div>
        <div class="label">Title</div>
        <div class="label">Control type</div>
    </div>
    <div class="wrapper2">
        <div class="text">${Subject.id}</div>
        <div class="text">${Subject.title}</div>
        <div class="text">${Subject.controlType}</div>
    </div>
</form>

<security:authorize access="hasRole('ROLE_ADMIN')">
<form action = "/subjects/${Subject.id}" method = "post" >
    <div class="updateWr1" >
        <h2 > Update subject</h2>
    </div >
    <div class="updateWr2" >
        <div class="label" > Title </div >
        <div class="label" > Control type </div >
    </div >
    <div class="updateWr3" >
        <div class="space" ><input type = "text" name = "title" required value="${Subject.title}" ></div >
        <div class="space" required >
            <select name="controlType">
                <option value="TEST">Test</option>
                <option value="EXAM">Exam</option>
                <option value="DIFFTEST">Diff test</option>
            </select>
        </div >
    </div >

    <div class="forSubmit" >
        <button type = "submit" class="btn" > Submit </button >
    </div >
</form>
</security:authorize>
<div class="forSubmit" >
    <button class="btn" onclick="location.href='/'">Menu</button>
</div >
</body>
<jsp:include page="../views/footer.jsp" />
</html>
