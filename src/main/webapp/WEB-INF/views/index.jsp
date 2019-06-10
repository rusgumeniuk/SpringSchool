<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Ruslanius home page</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>

<body>
<jsp:include page="../views/header.jsp" />
<div class="container">
    <div class="row">
        <h1>Welcome here!</h1>
    </div>
    <div class="container">
        <p><h2>On the our website you can look at:</h2></p>
        <ul>
            <li class="hyperlink"><a href="/subject">Subjects</a></li>
            <li class="hyperlink"><a href="/students">Students</a></li>
            <li class="hyperlink"><a href="/groups">Groups</a></li>
            <li class="hyperlink"><a href="/rooms">Rooms</a></li>
            <li class="hyperlink"><a href="/buildings">Buildings</a></li>
            <li class="hyperlink"><a href="/teachers">Teachers</a></li>
            <li class="hyperlink"><a href="/lessons">Lessons</a></li>
        </ul>
    </div>
    <security:authorize access="hasRole('ROLE_ADMIN')">
        <div class="row">
            <h3>And you are admin so you can do some additional functions:</h3>
            <ul class="list">
                <li class="hyperlink"><a href="/admins">Create new admin</a></li>
                <li class="hyperlink"><a href="/messages">Look at all messages</a></li>
                <li class="hyperlink"><a href="/allProperties">Check properties of all modules and do
                    bus-refresh</a></li>
            </ul>
        </div>
    </security:authorize>
</div>
</body>
<jsp:include page="../views/footer.jsp" />
</html>