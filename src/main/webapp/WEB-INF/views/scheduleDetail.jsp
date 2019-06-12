<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ page import="com.example.lessons.WeekMode" %>

            <!DOCTYPE>
            <html>

            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
                <title>Subject</title>
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
                <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
            </head>
            <jsp:include page="../views/header.jsp" />
            <body>
            <div class="container justify-content-center">
                <h1>${group != null ? group.title : "We don`t have group with same id"}</h1>
            </div>
<table class="table table-bordered col-7">
<c:forTokens items="0,1" delims="," var="week">
    <tr>
        <c:forEach items="${DaysOfWeek}" var="day">
            <th>
                <h3><b3>${day}</b3></h3>
            </th>
        </c:forEach>
    </tr>
    <tr>
        <c:forEach items="${DaysOfWeek}" var="day">
            <td>
                <table class="table">
                    <c:forEach items="${LessonNumbers}" var="number">
                        <tr>
                            <td>
                                <i scape="col">${number}</i>
                                <c:forEach items="${GroupLessons}" var="lesson">
                                    <c:if test="${lesson.lessonNumber == number && lesson.dayOfWeek == day}">
                                        <c:if test="${lesson.weekMode == WeekMode.valueOf('BOTH') || lesson.weekMode == WeekMode.values()[Integer.valueOf(week)]}">
                                            <br />
                                            <table>
                                                <tr>${lesson.subject.title}</tr><br />
                                                <tr style="font-size:x-small; align-content:flex-end">${lesson.lessonType}</tr><br />
                                                <tr>${lesson.teacher.fullName}</tr><br />
                                                <tr>${lesson.room.number}</tr><br />
                                            </table>
                                        </c:if>
                                    </c:if>
                                </c:forEach>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </td>
        </c:forEach>
    </tr>
</c:forTokens>
</table>
            </body>
            <jsp:include page="../views/footer.jsp" />

            </html>