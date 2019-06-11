<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
            <!DOCTYPE>
            <html>

            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
                <title>Teacher info</title>
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
                <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
            </head>
            <jsp:include page="../views/header.jsp" />

            <body>
                <div class="container justify-content-center col-8">
                    <h1>${error != null ? error : "Teacher list:"}</h1>
                    <table class="table">
                        <tbody class="tbody-dark">
                            <tr>
                                <th scape="col" class="text">Id</th>
                                <td class="text">${Teacher.id}</td>
                            </tr>
                            <tr>
                                <th scape="col" class="text">Full name</th>
                                <td class="text">${Teacher.fullName}</td>
                            </tr>
                            <tr>
                                <th scape="col" class="text">Rank</th>
                                <td class="text">${Teacher.teacherRank}</td>
                            </tr>
                            <tr>
                                <th scape="col" class="text">Cathdera</th>
                                <td class="text">${Teacher.cathedra}</td>
                            </tr>
                            <tr>
                                <th scape="col" class="text">Mentored group</th>
                                <td class="text">${Teacher.mentored_group.title}</td>
                            </tr>
                        </tbody>
                    </table>
                    <security:authorize access="hasRole('ROLE_ADMIN')">
                        <form action="/teachers/${Teacher.id}" method="post">
                            <div class="form-group">
                                <div class="title">
                                    <h2>Update teacher</h2>
                                </div>
                            </div>
                            <div class="form-row">
                                <div class="col">
                                    <div class="form-group">
                                        <div class="label"> Full name </div>
                                        <input class="form-control" type="text" name="fullName" required value="${Teacher.fullName}">
                                    </div>
                                </div>
                                <div class="col">
                                    <div class="form-group">
                                        <div class="label">Cathedra </div>
                                        <input class="form-control" type="text" name="cathedra" required value="${Teacher.cathedra}">
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

                    </security:authorize>
                </div>
            </body>
            <jsp:include page="../views/footer.jsp" />

            </html>