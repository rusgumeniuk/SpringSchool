<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
            <!DOCTYPE>
            <html>

            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
                <title>Building info</title>
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
                <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
            </head>
            <jsp:include page="../views/header.jsp" />

            <body>
                <div class="container table justify-content-center">
                    <h1>${error != null ? error : "Information about building:"}</h1>
                    <table class="table">
                        <tbody class="tbody-dark">
                            <tr>
                                <th scape="col" class="text">Id</th>
                                <td class="text">${Building.id}</td>
                            </tr>
                            <tr>
                                <th scape="col" class="text">Number</th>
                                <td class="text">${Building.number}</td>
                            </tr>
                            <tr>
                                <th scape="col" class="text">Count of storeys</th>
                                <td class="text">${Building.countOfStoreys}</td>
                            </tr>
                        </tbody>
                    </table>
                    <div class="container">
                        <p>
                            <h3>Rooms of this building:</h3>
                        </p>
                        <table class="table">
                            <tr>
                                <th scape="col">Id</th>
                                <th scape="col">Number</th>
                            </tr>
                            <c:forEach items="${Building.rooms}" var="room">
                                <tr>
                                    <td class="text text-light">${room.id}</td>
                                    <td class="text text-dark">${room.number}</td>
                                </tr>
                            </c:forEach>
                        </table>
                    </div>

                    <security:authorize access="hasRole('ROLE_ADMIN')">
                        <div class="row justify-content-center">
                            <div class="col-8">
                                <form action="/buildings/${Building.id}" method="post">
                                    <div class="form-group">
                                        <div class="title">
                                            <h2> Update building</h2>
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="col">
                                            <div class="form-group">
                                                <div class="label"> Number </div>
                                                <input class="form-control" type="number" min="1" max="50" name="number" required value="${Building.number}">
                                            </div>
                                        </div>
                                        <div class="col">
                                            <div class="form-group">
                                                <div class="label"> Count of storeys </div>
                                                <input class="form-control" type="number" min="1" max="50" name="countOfStoreys" required value="${Building.countOfStoreys}">
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