<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
            <!DOCTYPE>
            <html>

            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
                <title>Group info</title>
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
                <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
            </head>
            <jsp:include page="../views/header.jsp" />

            <body>
                <div class="container table justify-content-center">
                    <h1>${error != null ? error : "Information about group:"}</h1>
                    <table class="table">
                        <tbody class="tbody-dark">
                            <tr>
                                <th scape="col" class="text">Id</th>
                                <td class="text">${Group.id}</td>
                            </tr>
                            <tr>
                                <th scape="col" class="text">Title</th>
                                <td class="text">${Group.title}</td>
                            </tr>
                            <tr>
                                <th scape="col" class="text">Cathedra</th>
                                <td class="text">${Group.cathedra}</td>
                            </tr>
                            <tr>
                                <th scape="col" class="text">Start year</th>
                                <td class="text">${Group.startYear}</td>
                            </tr>
                            <%--<tr>
                                <th scape="col" class="text">Mentor</th>
                                <td class="text">${Group.mentor}</td>
                            </tr>--%>
                        </tbody>
                    </table>
                    <div class="container">
                        <p>
                            <h3>Students of this group:</h3>
                        </p>
                        <table class="table">
                            <tr>
                                <th scape="col">Id</th>
                                <th scape="col">Name</th>
                            </tr>
                            <c:forEach items="${Group.students}" var="student">
                                <tr>
                                    <td class="text text-light">${student.id}</td>
                                    <td class="text text-dark">${student.name}</td>
                                </tr>
                            </c:forEach>
                        </table>
                    </div>
                    <security:authorize access="hasRole('ROLE_ADMIN')">
                        <div class="row justify-content-center">
                            <div class="col-8">
                                <form action="/groups/${Group.id}" method="post">
                                    <div class="form-group">
                                        <div class="title">
                                            <h2> Update group</h2>
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="col">
                                            <div class="form-group">
                                                <label for="titleInput" class="label"> Title </label>
                                                <input class="form-control" id="titleInput" type="text" name="title" required value="${Group.title}">
                                            </div>
                                        </div>
                                        <div class="col">
                                            <div class="form-group">
                                                <div class="label"> Cathedra </div>
                                                <input class="form-control" type="text" name="cathedra" required value="${Group.cathedra}">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="col">
                                            <div class="form-group">
                                                <div class="label"> Start year </div>
                                                <input class="form-control" type="number" min="1980" max="2018" name="startYear" required value="${Group.startYear}">
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