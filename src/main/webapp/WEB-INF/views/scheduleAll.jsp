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
                    <form action="/schedules" method="post">
                        <div class="form-row">
                            <div class="col">
                                <div class="form-group">
                                    <label for="groupSelect">Select control type</label>
                                    <select class="form-control" name="groupId" id="groupSelect">
                                        <c:forEach items="${groupList}" var="group">
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
                </div>
            </body>
            <jsp:include page="../views/footer.jsp" />

            </html>