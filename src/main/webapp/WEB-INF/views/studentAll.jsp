<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
    <%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
            <!DOCTYPE>
            <html>

            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
                <title>Students</title>
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
                <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
            </head>
            <jsp:include page="../views/header.jsp" />

            <body>
                <h1>${result != null ? result : ""}</h1>
                <div class="container justify-content-center col-8">
                    <table class="table table-striped table-hover table-bordered col-8">
                        <thead class="thead-light">
                            <tr>
                                <th scape="col">Id</th>
                                <th scape="col">Name</th>
                                <%--<th scape="col">Group</th>--%>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${StudentList}" var="student">
                                <tr>
                                    <th scape="col" class="text">${student.id}</th>
                                    <td class="text">${student.name}</td>
                                    <%--<td class="text">${student.group.title}</td>--%>
                                    <td class="text">
                                        <a type="button" class="btn btn-sm btn-outline-secondary" href="/students/${student.id}">Detail</a>
                                        <security:authorize access="hasRole('ROLE_ADMIN')">
                                            <a type="button" class="btn btn-sm btn-outline-danger" href="/students/delete/${student.id}">Delete</a>
                                        </security:authorize>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <security:authorize access="hasRole('ROLE_ADMIN')">
                        <div class="row justify-content-center">
                            <div class="col-8">
                                <form action="/students" method="post">
                                    <div class="form-group">
                                        <div class="title">
                                            <h2>Add new student</h2>
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="col">
                                            <div class="form-group">
                                                <label for="nameInput" class="label"> Name </label>
                                                <input class="form-control" id="nameInput" type="text" name="name" required placeholder="Input name">
                                            </div>
                                        </div>
                                        <div class="col">
                                            <div class="form-group">
                                                <div class="label"> Age </div>
                                                <input class="form-control" type="number" min="16" max="100" name="age" required>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="col">
                                            <div class="form-group">
                                                <div class="label"> City </div>
                                                <input class="form-control" type="text" name="city" required placeholder="Input city">
                                            </div>
                                        </div>
                                        <div class="col">
                                            <div class="form-group">
                                                <label for="maleSelect">Select student's male</label>
                                                <select name="male" id="maleSelect" required class="form-control">
                                        <option value="MALE">Male</option>
                                        <option value="FEMALE">Female</option>
                                    </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="col">
                                            <div class="form-group">
                                                <label for="groupSelect">Select group</label>
                                                <select class="form-control" name="group" id="groupSelect">
                                        <c:forEach items="${Groups}" var="group">
                                            <option value="${group.id}">${group.title}</option>
                                        </c:forEach>
                                    </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="col-1">
                                            <div class="form-group justify-content-center">
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