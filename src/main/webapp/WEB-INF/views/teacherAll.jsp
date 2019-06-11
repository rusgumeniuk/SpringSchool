<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
    <%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
            <!DOCTYPE>
            <html>

            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
                <title>Teachers</title>
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
                <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
            </head>
            <jsp:include page="../views/header.jsp" />

            <body>
                <div class="container justify-content-center col-10">
                    <h1>${result != null ? result : ""}</h1>
                    <table class="table table-striped table-hover table-bordered col-10">
                        <thead class="thead-light">
                            <tr>
                                <th scape="col">Id</th>
                                <th scape="col">Full name</th>
                                <th scape="col">Cathedra</th>
                                <th scape="col">Metnored group</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${TeacherList}" var="teacher">
                                <tr>
                                    <th scape="col" class="text">${teacher.id}</th>
                                    <td class="text">${teacher.fullName}</td>
                                    <td class="text">${teacher.cathedra}</td>
                                    <td class="text">${teacher.mentored_group.title}</td>
                                    <td class="text">
                                        <button type="button" class="btn btn-sm btn-outline-secondary"> <a  href="/teachers/${teacher.id}">Detail</a></button>
                                        <security:authorize access="hasRole('ROLE_ADMIN')">
                                            <button type="button" class="btn btn-sm btn-outline-danger"><a href="/teachers/delete/${teacher.id}">Delete</a></button>
                                        </security:authorize>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <security:authorize access="hasRole('ROLE_ADMIN')">
                        <div class="row justify-content-center">
                            <div class="col-8">
                                <form action="/teachers" method="post">
                                    <div class="form-group">
                                        <div class="title">
                                            <h2>Add new teacher</h2>
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="col">
                                            <div class="form-group">
                                                <div class="label"> Full name </div>
                                                <input class="form-control" type="text" name="fullName" required placeholder="Input full name">
                                            </div>
                                        </div>
                                        <div class="col">
                                            <div class="form-group">
                                                <div class="label">Cathedra </div>
                                                <input class="form-control" type="text" name="cathedra" required placeholder="Input cathedra">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="col">
                                            <div class="form-group">
                                                <label for="rankSelect">Select teacher's rank</label>
                                                <select name="teacherRank" id="rankSelect" required class="form-control">
                                        <c:forEach items="${Ranks}" var="rank">
                                            <option value="${rank}">${rank}</option>
                                        </c:forEach>
                                    </select>
                                            </div>
                                        </div>
                                        <div class="col">
                                            <div class="form-group">
                                                <label for="groupSelect">Select mentored group</label>
                                                <select class="form-control" name="mentored_group" id="groupSelect">
                                        <c:forEach items="${Groups}" var="group">
                                            <option value="${group.id}">${group.title}</option>
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