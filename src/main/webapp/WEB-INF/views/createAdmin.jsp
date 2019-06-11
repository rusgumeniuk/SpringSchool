<!DOCTYPE>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Create new any user</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>
<jsp:include page="../views/header.jsp" />

<body>
    <div class="container col-6 justify-content-center">
        <h4> ${error != null ? "Some error" : ""}</h4>
        <form method="post" action="/admins">
            <div class="form-group">
                <label for="username">Input username:</label>
                <input name="username" id="username" type="text" class="form-control" placeholder="Username" autofocus="true" required />
                <br>
                <label for="password">Input password:</label>
                <input name="password" id="password" type="password" class="form-control" placeholder="Password" required />
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
                <button type="submit" class="btn btn-outline-success"> Create new user </button>
            </div>
        </form>
    </div>
</body>
<jsp:include page="../views/footer.jsp" />

</html>