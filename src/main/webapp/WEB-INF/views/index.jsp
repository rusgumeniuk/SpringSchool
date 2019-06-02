<!DOCTYPE>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Log in</title>
    <link href="../../resources/enterStyle.css" type="text/css" rel="stylesheet" />
</head>
<jsp:include page="../views/header.jsp" />
<body>
<h1>HERE U R </h1>
<div>
    <form method = "post" action="${contextPath}">
        <h2>Log in</h2>

        <div class="form-group ${error != null ? 'has-error' : ''}">
            <span style="margin-bottom: 10px">${msg}</span>
            <input name="username" type="text" placeholder="Username" autofocus="true"/>
            <br>
            <input name="password" type="password" placeholder="Password"/>
            <span style="margin-top: 10px">${errorMsg}</span>
            <br>

            <button type="submit">Log In</button>
            <a href="/registration" >Sign in</a>
        </div>
    </form>
</div>
</body>
</html>