<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Log in</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">

</head>

<body>
<div class="container-fluid">
    <div class="row">
        <form method="post" action="/login"  style="max-width: 600px;padding-left: 0;margin-top: 30px;" class="col-7 offset-4">
            <h2 class="form-heading">Welcome in da Ruslanius school</h2><br>
            <h2 class="form-heading">Please, log in firstly</h2><br>

            <div class="form-group ${error != null ? 'has-error' : null}">
                <span style="margin-bottom: 10px">${msg}</span>
                <input name="username" type="text" required class="form-control" placeholder="Username"
                       autofocus="true"/>
                <br>
                <input name="password" type="password" required class="form-control" placeholder="Password"/>
                <span style="margin-top: 10px">${errorMsg}</span>
                <br>

                <button class="btn btn-lg btn-primary btn-block" type="submit">Log in</button>
                <a href="/registration" class="btn btn-lg btn-link btn-block" >Sign up</a>
            </div>

        </form>
    </div>
</div>
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
</body>
</html>
