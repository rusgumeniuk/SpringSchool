<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Lesson info</title>
    <link href="../../resources/groupDetailStyle.css" type="text/css" rel="stylesheet" />
</head>
<jsp:include page="../views/header.jsp" />
<body>
<h1>${error != null ? error : "Information about lesson:"}</h1>
<form action="/lessons/{id}" method="get">
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
    <div class="wrapper2">
        <div class="text">${Lesson.id}</div>
        <div class="text">${Lesson.lessonNumber}</div>
        <div class="text">${Lesson.lessonType}</div>
        <div class="text">${Lesson.weekMode}</div>
        <div class="text">${Lesson.dayOfWeek}</div>
        <div class="text">${Room.number}</div>
        <div class="text">${Subject.title}</div>
        <div class="text">${Teacher.fullName}</div>
        <div class="text">${Group.title}</div>
    </div>
</form>

<security:authorize access="hasRole('ROLE_ADMIN')">
<form action = "/lessons/${Lesson.id}" method = "post" >
    <div class="updateWr1" >
        <h2 > Update lesson</h2>
    </div >
    <div class="updateWr2" >
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
            <select name="group" >
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
<div class="goBack" >
    <button class="btn" onclick="location.href='/'">Menu</button>
</div >

</body>
<jsp:include page="../views/footer.jsp" />
</html>
