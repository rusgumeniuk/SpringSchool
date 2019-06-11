<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
            <!DOCTYPE>
            <html>

            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
                <title>Subject</title>
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
                <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
            </head>
            <jsp:include page="../views/header.jsp" />

            <body>
                <div class="container table justify-content-center">
                    <h1>${error != null ? error : "Information about subject:"}</h1>
                    <table class="table">
                        <tbody class="tbody-dark">
                            <tr>
                                <th scape="col" class="text">Id</th>
                                <td class="text">${Subject.id}</td>
                            </tr>
                            <tr>
                                <th scape="col" class="text">Title</th>
                                <td class="text">${Subject.title}</td>
                            </tr>
                            <tr>
                                <th scape="col" class="text">Male</th>
                                <td class="text">${Subject.controlType}</td>
                            </tr>
                        </tbody>
                    </table>
                    <security:authorize access="hasRole('ROLE_ADMIN')">
                        <div class="row justify-content-center">
                            <div class="col-8">
                                <form action="/subjects/${Subject.id}" method="post">
                                    <div class="form-group">
                                        <div class="title">
                                            <h2>Add new subject</h2>
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="col">
                                            <div class="form-group">
                                                <label for="titleInput" class="label"> Title </label>
                                                <input class="form-control" id="titleInput" type="text" name="title" required value="${Subject.title}">
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
            </body>
            <jsp:include page="../views/footer.jsp" />

            </html>