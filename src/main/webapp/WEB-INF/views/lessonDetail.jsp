<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
            <!DOCTYPE>
            <html>

            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
                <title>Lesson info</title>
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
                <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
            </head>
            <jsp:include page="../views/header.jsp" />

            <body>
                <div class="container table justify-content-center">
                    <h1>${error != null ? error : "Information about student:"}</h1>
                    <table class="table">
                        <tbody class="tbody-dark">
                            <tr>
                                <th scape="col" class="text">Id</th>
                                <td class="text">${Lesson.id}</td>
                            </tr>
                            <tr>
                                <th scape="col" class="text">lessonNumber</th>
                                <td class="text">${Lesson.lessonNumber}</td>
                            </tr>
                            <tr>
                                <th scape="col" class="text">lessonType</th>
                                <td class="text">${Lesson.lessonType}</td>
                            </tr>
                            <tr>
                                <th scape="col" class="text">dayOfWeek</th>
                                <td class="text">${Lesson.dayOfWeek}</td>
                            </tr>
                            <tr>
                                <th scape="col" class="text">weekMode</th>
                                <td class="text">${Lesson.weekMode}</td>
                            </tr>
                            <tr>
                                <th scape="col" class="text">Teacher</th>
                                <td class="text">${Lesson.teacher.fullName}</td>
                            </tr>
                            <tr>
                                <th scape="col" class="text">Subject</th>
                                <td class="text">${Lesson.subject.title}</td>
                            </tr>
                            <tr>
                                <th scape="col" class="text">Group</th>
                                <td class="text">${Lesson.group.title}</td>
                            </tr>
                            <tr>
                                <th scape="col" class="text">Room</th>
                                <td class="text">${Lesson.room.number}</td>
                            </tr>
                        </tbody>
                    </table>
                    <security:authorize access="hasRole('ROLE_ADMIN')">
                        <div class="row justify-content-center">
                            <div class="col-8">
                                <form action="/lessons/${Lesson.id}" method="post">
                                    <div class="form-group">
                                        <div class="title">
                                            <h2>Update lesson</h2>
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