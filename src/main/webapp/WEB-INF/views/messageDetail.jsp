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
                <security:authorize access="hasRole('ROLE_ADMIN')">
                    <div class="container justify-content-center col-8">
                        <table class="table table-striped table-hover table-bordered col-8">
                            <tr>
                                <th scape="col">Id</th>
                                <td class="text">${Message.id}</td>
                            </tr>
                            <tr>
                                <th scape="col">Class name</th>
                                <td class="text">${Message.className}</td>
                            </tr>
                            <tr>
                                <th scape="col">Status code</th>
                                <td class="text">${Message.statusCode}</td>
                            </tr>
                            <tr>
                                <th scape="col">Http method</th>
                                <td class="text">${Message.httpMethod}</td>
                            </tr>
                            <tr>
                                <th scape="col">Timestamp</th>
                                <td class="text">${Message.dateTime}</td>
                            </tr>
                            <tr>
                                <th scape="col">Error</th>
                                <td class="text">${Message.error}</td>
                            </tr>
                            <tr>
                                <th scape="col">Description</th>
                                <td class="text">${Message.description}</td>
                            </tr>
                        </table>
                    </div>
            </body>
            </security:authorize>
            <jsp:include page="../views/footer.jsp" />

            </html>