<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Lessons</title>
    <link href="../../resources/studentAllstyle.css" type="text/css" rel="stylesheet" />
</head>
<jsp:include page="../views/header.jsp" />
<body>
<h1>${result != null ? result : ""}</h1>
<form action="/lessons" method="get">
    <div class="wrapper1">
        <div class="label">Id</div>
        <div class="label">Order number</div>
        <div class="label">Lesson type</div>
        <div class="label">Week mode</div>
        <div class="label">Day of week</div>
        <div class="label">Room</div>
        <div class="label">Subject</div>
        <div class="label">Teacher</div>
        <div class="label">Group</div>
    </div>
    <div>
        <c:forEach  items="${LessonList}" var ="lesson">
            <div class="wrapper2">
                <div class="text">${lesson.id}</div>
                <div class="text">${lesson.lessonNumber}</div>
                <div class="text">${lesson.lessonType}</div>
                <div class="text">${lesson.weekMode}</div>
                <div class="text">${lesson.dayOfWeek}</div>
                <div class="text">${lesson.room.number}</div>
                <div class="text">${lesson.subject.title}</div>
                <div class="text">${lesson.teacher.fullName}</div>
                <div class="text">${lesson.group.title}</div>
                <div class="text">
                    <a type="button", class="btnAction", href="/lessons/${lesson.id}">Detail</a>
                    <security:authorize access="hasRole('ROLE_ADMIN')">
                        <a type="button", class="btnAction", href="/lessons/delete/${lesson.id}">Delete</a>
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
    <form action = "/lessons" method = "post" >
        <div class="createWr1" >
            <h2> Add new lesson</h2>
        </div>
        <div class="createWr2" >
            <div class="label">Id</div>
            <div class="label">Order number</div>
            <div class="label">Lesson type</div>
            <div class="label">Week mode</div>
            <div class="label">Day of week</div>
            <div class="label">Room</div>
            <div class="label">Subject</div>
            <div class="label">Teacher</div>
            <div class="label">Group</div>
        </div >
        <div class="createWr3" >
            <div class="space">
                <select required name="lessonNumber">
                    <c:forEach items="${LessonNumbers}" var="lessonNumber" >
                        <option value="${lessonNumber}">${lessonNumber}</option>
                    </c:forEach>
                </select>
            </div >
            <div class="space">
                <select required name="lessonType">
                    <c:forEach items="${LessonTypes}" var="lessonType" >
                        <option value="${lessonType}">${lessonType}</option>
                    </c:forEach>
                </select>
            </div >
            <div class="space">
                <select required name="weekMode">
                    <c:forEach items="${WeekModes}" var="weekMode" >
                        <option value="${weekMode}">${weekMode}</option>
                    </c:forEach>
                </select>
            </div >
            <div class="space">
                <select required name="dayOfWeek">
                    <c:forEach items="${DaysOfWeek}" var="dayOfWeek" >
                        <option value="${dayOfWeek}">${dayOfWeek}</option>
                    </c:forEach>
                </select>
            </div >
            <div class="space">
                <select required name="room">
                    <c:forEach items="${Rooms}" var="room" >
                        <option value="${room.id}">${room.number} : ${room.building != null ? room.building : " empty build."}</option>
                    </c:forEach>
                </select>
            </div >
            <div class="space">
                <select required name="subject">
                    <c:forEach items="${Subjects}" var="subject" >
                        <option value="${subject.id}">${subject.title}</option>
                    </c:forEach>
                </select>
            </div >
            <div class="space">
                <select required name="teacher">
                    <c:forEach items="${Teachers}" var="teacher" >
                        <option value="${teacher.id}">${teacher.fullName}</option>
                    </c:forEach>
                </select>
            </div >
            <div class="space" >
                <select required name="group" >
                    <c:forEach items="${Groups}" var="group">
                        <option value="${group.id}">${group.title}</option>
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
