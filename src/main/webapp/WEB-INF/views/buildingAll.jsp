<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
            <!DOCTYPE>
            <html>

            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
                <title>Buildings</title>
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
                <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
            </head>
            <jsp:include page="../views/header.jsp" />

            <body>
                <div class="container justify-content-center col-8">
                    <h1>${result != null ? result : ""}</h1>
                    <table class="table table-striped table-hover table-bordered col-8">
                        <thead class="thead-light">
                            <tr>
                                <th scape="col">Id</th>
                                <th scape="col">Number</th>
                                <th scape="col">Count of storeys</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${BuildingList}" var="building">
                                <tr>
                                    <th scape="col" class="text">${building.id}</th>
                                    <td class="text">${building.number}</td>
                                    <td class="text">${building.countOfStoreys}</td>
                                    <td class="justify-content-between">
                                        <button type="button" class="btn btn-sm btn-outline-secondary"> <a
                                    href="/buildings/${building.id}">Detail</a></button>
                                        <security:authorize access="hasRole('ROLE_ADMIN')">
                                            <button type="button" class="btn btn-sm btn-outline-danger"><a
                                        href="/buildings/delete/${building.id}">Delete</a></button>
                                        </security:authorize>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>

                    <security:authorize access="hasRole('ROLE_ADMIN')">
                        <div class="row justify-content-center">
                            <div class="col-8">
                                <form action="/buildings" method="post">
                                    <div class="form-group">
                                        <div class="title">
                                            <h2>Add new building</h2>
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="col">
                                            <div class="form-group">
                                                <div class="label"> Number </div>
                                                <input class="form-control" type="number" min="1" max="50" name="number" required>
                                            </div>
                                        </div>
                                        <div class="col">
                                            <div class="form-group">
                                                <div class="label"> Count of storeys </div>
                                                <input class="form-control" type="number" min="1" max="50" name="countOfStoreys" required>
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