<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
            <!DOCTYPE>
            <html>

            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
                <title>Groups</title>
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
                <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
            </head>
            <jsp:include page="../views/header.jsp" />

            <body>
                <div class="container justify-content-center col-8">
                    <table class="table table-striped table-hover table-bordered col-8">
                        <thead class="thead-light">
                            <tr>
                                <th scape="col">Id</th>
                                <th scape="col">Title</th>
                                <th scape="col">Cathedra</th>
                                <th scape="col">Start year</th>
                                <th scape="col">Mentor</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${GroupList}" var="group">
                                <tr>
                                    <th scape="col" class="text">${group.id}</th>
                                    <td class="text">${group.title}</td>
                                    <td class="text">${group.cathedra}</td>
                                    <td class="text">${group.startYear}</td>
                                    <td class="text">${group.mentor.fullName}</td>
                                    <td class="text">
                                        <a type="button" class="btn btn-sm btn-outline-secondary" href="/groups/${group.id}">Detail</a>
                                        <security:authorize access="hasRole('ROLE_ADMIN')">
                                            <a type="button" class="btn btn-sm btn-outline-danger" href="/groups/delete/${group.id}">Delete</a>
                                        </security:authorize>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <security:authorize access="hasRole('ROLE_ADMIN')">
                        <div class="row justify-content-center">
                            <div class="col-8">
                                <form action="/groups" method="post">
                                    <div class="form-group">
                                        <div class="title">
                                            <h2>Add new group</h2>
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="col">
                                            <div class="form-group">
                                                <label for="titleInput" class="label"> Title </label>
                                                <input class="form-control" id="titleInput" type="text" name="title" required placeholder="Input title">
                                            </div>
                                        </div>
                                        <div class="col">
                                            <div class="form-group">
                                                <div class="label"> Cathedra </div>
                                                <input class="form-control" type="text" name="cathedra" required placeholder="Input cathedra">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="col">
                                            <div class="form-group">
                                                <div class="label"> Start year </div>
                                                <input class="form-control" type="number" min="1980" max="2018" name="startYear" required>
                                            </div>
                                        </div>
                                        <div class="col">
                                            <div class="form-group">
                                                <label for="mentorSelect">Select mentor</label>
                                                <select class="form-control" name="group" id="mentorSelect">
                                                    <c:forEach items="${Mentors}" var="mentor">
                                                        <option value="${mentor.id}">${mentor.fullName}</option>
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