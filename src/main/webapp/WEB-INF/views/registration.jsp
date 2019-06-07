<!DOCTYPE>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Sign in</title>
    <link href="../../resources/registrationStyle.css" type="text/css" rel="stylesheet" />
</head>
<body>
<div>
    <div>
        <form method="post" action="/registration">
            <h2>Sign in</h2>

            <div class="form-group ${error != null ? 'has-error' : ''}">
                <label for="username">Input username:</label>
                <input name="username"  id="username" type="text" class="form-control" placeholder="Username"
                       autofocus="true"/>
                <br>
                <label for="password">Input password:</label>
                <input name="password" id="password" type="password" class="form-control" placeholder="Password"/>
                <span>${errorMsg}</span>
                <br>
                <div>
                    <label for="role">Select role:</label>
                    <select name="role" id="role" about="Here u can sign up only like user">
                        <option>User</option>
                    </select>
                </div>
                <br>
                <button type="submit">Sign in</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>