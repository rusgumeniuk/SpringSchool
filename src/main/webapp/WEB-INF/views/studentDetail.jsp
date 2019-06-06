<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Student info</title>
    <link href="../../resources/groupDetailStyle.css" type="text/css" rel="stylesheet" />
</head>
<jsp:include page="../views/header.jsp" />
<body>
<h1>${error != null ? error : "Information about student:"}</h1>
<form action="/students/{id}" method="get">
    <div class="wrapper1">
        <div class="label">Id</div>
        <div class="label">Name</div>
        <div class="label">Male</div>
        <div class="label">City</div>
        <div class="label">Age</div>
        <div class="label">Group</div>
    </div>
    <div class="wrapper2">
        <div class="text">${Student.id}</div>
        <div class="text">${Student.name}</div>
        <div class="text">${Student.male}</div>
        <div class="text">${Student.age}</div>
        <div class="text">${Student.city}</div>
        <div class="text">${Student.group}</div>

    </div>
</form>

<form action = "/students/${Student.id}" method = "post" >
    <div class="updateWr1" >
        <h2 > Update student</h2>
    </div >
    <div class="updateWr2" >
        <div class="label" > Name </div >
        <div class="label" > City </div >
        <div class="label" > Age </div >
        <div class="label" > Male </div >
        <div class="label" > Group </div >
    </div >
    <div class="updateWr3" >
        <div class="space" ><input type = "text" name = "name" required value="${Student.name}" ></div >
        <div class="space" ><input type = "text" name = "city" required value="${Student.city}" ></div >
        <div class="space" ><input type = "number" name = "age" min="16" max="100" required value="${Student.age}" ></div >
        <div class="space" >
            <select name="male">
                <option selected="${Student.male == Male.MALE}" value="MALE">Male</option>
                <option selected="${Student.male == Male.FEMALE}" value="FEMALE">Female</option>
            </select>
        </div >
        <div class="space" >
            <select name="group">
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

<div class="goBack" >
    <button class="btn" onclick="location.href='/menu'">Menu</button>
</div >

</body>
<jsp:include page="../views/footer.jsp" />
</html>
