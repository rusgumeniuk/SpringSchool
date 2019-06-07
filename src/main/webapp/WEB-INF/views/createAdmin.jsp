<!DOCTYPE>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Sign in</title>
    <link href="../../resources/registrationStyle.css" type="text/css" rel="stylesheet" />
</head>
<jsp:include page="../views/header.jsp" />
<body>
<div>
    <div>
        <form method="post" action="/admins">
            <h2>Sign un</h2>

            <div class="form-group ${error != null ? 'has-error' : ''}">
                <label for="username">Input username:</label>
                <input name="username"  id="username" type="text" required class="form-control" placeholder="Username"
                       autofocus="true"/>
                <br>
                <label for="password">Input password:</label>
                <input name="password" required id="password" type="password" class="form-control" placeholder="Password"/>
                <span>${errorMsg}</span>
                <br>
                <div>
                    <label for="role">Select role:</label>
                    <select required name="role" id="role">
                        <option>Admin</option>
                        <option>User</option>
                    </select>
                </div>
                <br>
                <button type="submit">Sign up</button>
            </div>
        </form>
    </div>
</div>
</body>
<jsp:include page="../views/footer.jsp" />
</html>