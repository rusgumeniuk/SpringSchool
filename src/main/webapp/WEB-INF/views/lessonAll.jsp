<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
    <%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
            <!DOCTYPE>
            <html>

            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
                <title>Lessons</title>
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
                <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
            </head>
            <jsp:include page="../views/header.jsp" />

            <body>
                <h1>${result != null ? result : ""}</h1>
                <div class="container justify-content-center col-12">
                    <table class="table table-striped table-hover table-bordered col-12">
                        <thead class="thead-light">
                            <tr>
                                <th scape="col">Id</th>
                                <th scape="col">Lesson type</th>
                                <th scape="col">Lesson number</th>
                                <th scape="col">Week mode</th>
                                <th scape="col">Day of week</th>
                                <th scape="col">Teacher</th>
                                <th scape="col">Subject</th>
                                <th scape="col">Group</th>
                                <th scape="col">Room</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${LessonList}" var="lesson">
                                <tr>
                                    <th scape="col" class="text">${lesson.id}</th>
                                    <td class="text">${lesson.lessonNumber}</td>
                                    <td class="text">${lesson.lessonType}</td>
                                    <td class="text">${lesson.weekMode}</td>
                                    <td class="text">${lesson.dayOfWeek}</td>
                                    <td class="text">${lesson.teacher.fullName}</td>
                                    <td class="text">${lesson.subject.title}</td>
                                    <td class="text">${lesson.group.title}</td>
                                    <td class="text">${lesson.room.number}</td>
                                    <td class="text">
                                        <a type="button" class="btn btn-sm btn-outline-secondary" href="/lessons/${lesson.id}">Detail</a>
                                        <security:authorize access="hasRole('ROLE_ADMIN')">
                                            <a type="button" class="btn btn-sm btn-outline-danger" href="/lessons/delete/${lesson.id}">Delete</a>
                                        </security:authorize>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <security:authorize access="hasRole('ROLE_ADMIN')">
                        <div class="row justify-content-center">
                            <div class="col-8">
                                <form action="/lessons" method="post">
                                    <div class="form-group">
                                        <div class="title">
                                            <h2>Add new lesson</h2>
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="col">
                                            <div class="form-group">
                                                <label for="groupSelect">Lesson number</label>
                                                <select class="form-control" name="lessonNumber" id="controlTypeSelect">
                                        <c:forEach items="${LessonNumbers}" var="lessonNumber">
                                            <option value="${lessonNumber}">${lessonNumber}</option>
                                        </c:forEach>
                                    </select>
                                            </div>
                                        </div>
                                        <div class="col">
                                            <div class="form-group">
                                                <label for="groupSelect">Select lesson type</label>
                                                <select class="form-control" name="lessonType" id="controlTypeSelect">
                                        <c:forEach items="${LessonTypes}" var="lessonType">
                                            <option value="${lessonType}">${lessonType}</option>
                                        </c:forEach>
                                    </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="col">
                                            <div class="form-group">
                                                <label for="groupSelect">Select day of week</label>
                                                <select class="form-control" name="dayOfWeek" id="controlTypeSelect">
                                        <c:forEach items="${DaysOfWeek}" var="dayOfWeek">
                                            <option value="${dayOfWeek}">${dayOfWeek}</option>
                                        </c:forEach>
                                    </select>
                                            </div>
                                        </div>
                                        <div class="col">
                                            <div class="form-group">
                                                <label for="groupSelect">Select week mode</label>
                                                <select class="form-control" name="weekMode" id="controlTypeSelect">
                                        <c:forEach items="${WeekModes}" var="weekMode">
                                            <option value="${weekMode}">${weekMode}</option>
                                        </c:forEach>
                                    </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="col">
                                            <div class="form-group">
                                                <label for="groupSelect">Select teacher</label>
                                                <select class="form-control" name="teacher" id="controlTypeSelect">
                                        <c:forEach items="${Teachers}" var="teacher">
                                            <option value="${teacher}">${teacher.fullName}</option>
                                        </c:forEach>
                                    </select>
                                            </div>
                                        </div>
                                        <div class="col">
                                            <div class="form-group">
                                                <label for="groupSelect">Select subject</label>
                                                <select class="form-control" name="controlType" id="controlTypeSelect">
                                        <c:forEach items="${Subjects}" var="subject">
                                            <option value="${subject}">${subject.title}</option>
                                        </c:forEach>
                                    </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="col">
                                            <div class="form-group">
                                                <label for="groupSelect">Select group</label>
                                                <select class="form-control" name="controlType" id="controlTypeSelect">
                                        <c:forEach items="${Groups}" var="group">
                                            <option value="${group}">${group.title}</option>
                                        </c:forEach>
                                    </select>
                                            </div>
                                        </div>
                                        <div class="col">
                                            <div class="form-group">
                                                <label for="groupSelect">Select room</label>
                                                <select class="form-control" name="controlType" id="controlTypeSelect">
                                        <c:forEach items="${Rooms}" var="room">
                                            <option value="${room}">${room.number}</option>
                                        </c:forEach>
                                    </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="col">
                                            <div class="form-group">
                                                <div class="forSubmit">
                                                    <button type="submit" class="btn btn-outline-success"> Submit </button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </security:authorize>
                </div>
            </body>
            <jsp:include page="../views/footer.jsp" />

            </html>