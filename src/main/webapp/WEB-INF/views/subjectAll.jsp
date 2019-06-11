<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
            <!DOCTYPE>
            <html>

            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
                <title>Subjects</title>
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
                                <th scape="col">Title</th>
                                <th scape="col">Control type</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${SubjectList}" var="subject">
                                <tr>
                                    <th scape="col" class="text">${subject.id}</th>
                                    <td class="text">${subject.title}</td>
                                    <td class="text">${subject.controlType}</td>
                                    <td class="text">
                                        <a type="button" class="btn btn-sm btn-outline-secondary" href="/subjects/${subject.id}">Detail</a>
                                        <security:authorize access="hasRole('ROLE_ADMIN')">
                                            <a type="button" class="btn btn-sm btn-outline-danger" href="/subjects/delete/${subject.id}">Delete</a>
                                        </security:authorize>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <security:authorize access="hasRole('ROLE_ADMIN')">
                        <div class="row justify-content-center">
                            <div class="col-8">
                                <form action="/subjects" method="post">
                                    <div class="form-group">
                                        <div class="title">
                                            <h2>Add new subject</h2>
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
                                                <label for="groupSelect">Select control type</label>
                                                <select class="form-control" name="controlType" id="controlTypeSelect">
                                        <c:forEach items="${ControlTypes}" var="controlType">
                                            <option value="${controlType}">${controlType}</option>
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