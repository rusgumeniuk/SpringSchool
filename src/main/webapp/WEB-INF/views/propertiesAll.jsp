<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
            <!DOCTYPE>
            <html>

            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
                <title>Messages</title>
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
                <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
            </head>
            <jsp:include page="../views/header.jsp" />

            <body>
                <security:authorize access="hasRole('ROLE_ADMIN')">
                    <div class="container col-8 justify-content-center">
                        <h3>${result != null ? result : ""}</h3>
                        <h2>Properties from all modules:</h2>
                        <table class="table table-bordered">
                            <tr>
                                <th scape="col">School service's properties</th>
                                <td class="text">${school}</td>
                            </tr>
                            <tr>
                                <th scape="col">Lesson service's properties:</th>
                                <td class="text">${lesson}</td>
                            </tr>
                            <tr>
                                <th scape="col">Server's properties:</th>
                                <td class="text">${server}</td>
                            </tr>
                            <tr>
                                <th scape="col">Client's properties:</th>
                                <td class="text">${client}</td>
                            </tr>
                        </table>
                        <form action="/updateAllProperties" method="post">
                            <h2>Update all properties from all modules</h2>
                            <button type="submit" class="btn btn-outline-success"> Do bus-refresh </button>
                        </form>
                </security:authorize>
                </div>
            </body>
            <jsp:include page="../views/footer.jsp" />

            </html>