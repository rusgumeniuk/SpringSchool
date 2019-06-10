<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
            <!DOCTYPE>
            <html>

            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
                <title>Student info</title>
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
                                <td class="text">${Student.id}</td>
                            </tr>
                            <tr>
                                <th scape="col" class="text">Name</th>
                                <td class="text">${Student.name}</td>
                            </tr>
                            <tr>
                                <th scape="col" class="text">Male</th>
                                <td class="text">${Student.male}</td>
                            </tr>
                            <tr>
                                <th scape="col" class="text">City</th>
                                <td class="text">${Student.city}</td>
                            </tr>
                            <tr>
                                <th scape="col" class="text">Age</th>
                                <td class="text">${Student.age}</td>
                            </tr>
                            <tr>
                                <th scape="col" class="text">Group</th>
                                <td class="text">${Student.group.title}</td>
                            </tr>
                        </tbody>
                    </table>
                    <security:authorize access="hasRole('ROLE_ADMIN')">
                        <form action="/students/${Student.id}" method="post">
                            <div class="form-group">
                                <div class="title">
                                    <h2> Update student</h2>
                                </div>
                            </div>
                            <div class="form-row">
                                <div class="col">
                                    <div class="form-group">
                                        <label for="nameInput" class="label"> Name </label>
                                        <input class="form-control" id="nameInput" type="text" name="name" value="${Student.name}" required>
                                    </div>
                                </div>
                                <div class="col">
                                    <div class="form-group">
                                        <div class="label"> Age </div>
                                        <input class="form-control" type="number" min="16" max="100" name="age" required value="${Student.age}">
                                    </div>
                                </div>
                            </div>
                            <div class="form-row">
                                <div class="col">
                                    <div class="form-group">
                                        <div class="label"> City </div>
                                        <input class="form-control" type="text" name="city" required value="${Student.city}">
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
                                <div class="col" aria-rowspan="2">
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