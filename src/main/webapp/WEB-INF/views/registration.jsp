<!DOCTYPE>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Sign up</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>

<body>
    <div class="container col-6 justify-content-center">
        <form method="post" action="/registration">
            <h2>Sign up</h2>
            <h3>${error != null ? "Some Error" : "" }</h3>
            <div class="form-group">
                <label for="username">Input username:</label>
                <input name="username" id="username" type="text" class="form-control" placeholder="Username" autofocus="true" required />
                <br>
                <label for="password">Input password:</label>
                <input name="password" id="password" type="password" class="form-control" placeholder="Password" required />
                <span>${errorMsg}</span>
                <br>
                <div>
                    <label for="role">Select a role:</label>
                    <select name="role" id="role" about="Here u can sign up only like user">
                        <option>User</option>
                    </select>
                </div>
                <br>
                <div class="row justify-content-around">
                    <button class="btn btn-outline-success" type="submit">Sign up</button>
                    <button class="btn btn-outline-info"><a href="/login">Back to the login page</a></button>
                </div>

            </div>
        </form>

    </div>
</body>

</html>