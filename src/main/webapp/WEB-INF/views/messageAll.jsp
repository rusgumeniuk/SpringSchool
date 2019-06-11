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
                    <div class="container justify-content-center col-11">
                        <div>
                            <h2>
                                Oh, you are an admin and u can see next:
                            </h2>
                        </div>

                        <table class="table table-bordered col-11">
                            <c:forEach items="${messageList}" var="message">
                                <tr>
                                    <tr>
                                        <th scape="col" rowspan="2" style="vertical-align: center; align-content:center;">
                                            ${message.className}
                                        </th>
                                        <td>${message.httpMethod}</td>
                                        <td>${message.statusCode}</td>
                                        <td rowspan="2">
                                            ${message.description}
                                        </td>
                                        <td rowspan="2" colspan="2">${message.error}</td>
                                        <td rowspan="2">
                                            <a type="button" class="btn btn-sm btn-outline-secondary" href="/messages/${message.id}">Detail</a>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="2">${message.dateTime}</td>
                                    </tr>

                                </tr>
                            </c:forEach>
                        </table>
                    </div>
                </security:authorize>
            </body>
            <jsp:include page="../views/footer.jsp" />

            </html>