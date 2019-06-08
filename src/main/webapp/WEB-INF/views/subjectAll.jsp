<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Subjects</title>
    <link href="../../resources/subjectAllstyle.css" type="text/css" rel="stylesheet" />
</head>
<jsp:include page="../views/header.jsp" />
<body>
<h1>${result != null ? result : ""}</h1>
<form action="/subjects" method="get">
    <div class="wrapper1">
        <div class="label">Id</div>
        <div class="label">Title</div>
        <div class="label">Control type</div>
    </div>
    <div>
        <c:forEach  items="${SubjectList}" var ="subject">
            <div class="wrapper2">
                <div class="text">${subject.id}</div>
                <div class="text">${subject.title}</div>
                <div class="text">${subject.controlType}</div>
                <div class="text">
                    <a type="button", class="btnAction", href="/subjects/${subject.id}">Detail</a>
                    <security:authorize access="hasRole('ROLE_ADMIN')">
                        <a type="button", class="btnAction", href="/subjects/delete/${subject.id}">Delete</a>
                    </security:authorize>
                </div>
            </div>
        </c:forEach>
    </div>
</form>

<div class="forSubmit"  align="center" >
    <button class="btn"  onclick="location.href='/'">Menu</button>
</div >
<security:authorize access="hasRole('ROLE_ADMIN')">
<form action = "/subjects" method = "post" >
    <div class="createWr1" >
        <h2> Add new subject</h2>
    </div>
    <div class="createWr2" >
        <div class="label" > Title </div >
        <div class="label" > Control type </div >
    </div >
    <div class="createWr3" >
        <div class="space" ><input type = "text" name = "title" required ></div >
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
</body>
<jsp:include page="../views/footer.jsp" />
</html>
