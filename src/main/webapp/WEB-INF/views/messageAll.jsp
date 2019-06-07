<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Messages</title>
    <link href="../../resources/subjectAllstyle.css" type="text/css" rel="stylesheet" />
</head>
<jsp:include page="../views/header.jsp" />
<body>
<security:authorize access="hasRole('ROLE_ADMIN')">
    <div>
        <h2>
            Oh, you are an admin and u can see next:
        </h2>
    </div>
    <form action="/messages" method="get">
        <table border="1" cols>
            <caption>Messages:</caption>
            <c:forEach items = "${messageList}" var="message">
            <tr>
                <tr>
                    <td>${message.httpMethod}</td>
                    <td>${message.statusCode}</td>
                    <td rowspan="2">
                            ${message.description}
                    </td>
                    <td rowspan="2">${message.error}</td>
                    <td rowspan="2">
                        <a type="button", class="btnAction", href="/messages/${message.msg_id}">Detail</a>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">${message.dateTime}</td>
                </tr>

            </tr>
            </c:forEach>
        </table>
    </form>
    <div class="forSubmit" align="center">
        <button class="btn" onclick="location.href='../..'">Index</button>
    </div>
</security:authorize>
</body>
<jsp:include page="../views/footer.jsp" />
</html>