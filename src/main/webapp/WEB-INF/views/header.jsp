<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
    <html>

    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    </head>

    <body>
        <div class="container justify-content-center">
            <table class="table table-bordered" style="margin-left: 20px; margin-right: 20px">
                <tr>
                    <td colspan="4" style="text-align: center">
                        <a class="nav-link" href="/">
                            <h1 style="font-family:cursive"><i>Ruslanius school</i></h1>
                        </a>
                    </td>

                    <td colspan="2" style="text-align: center; vertical-align:center">
                        <label>User: "<security:authentication property="principal.username" />"</label>
                    </td>
                    <td colspan="2"  align="center" style="align-content: center; justify-content: center">
                        <a class="nav-link" href="/logout"> Log out</a>
                    </td>
                </tr>
                <tr>
                    <div class="row justify-content-center">
                        <div>
                            <nav class="nav nav-tabs">
                                <td>
                                    <a class="nav-item nav-link" href="/students">Students</a>
                                </td>
                                <td>
                                    <a class="nav-item nav-link" href="/groups">Groups</a>
                                </td>
                                <td>
                                    <a class="nav-item nav-link" href="/subjects">Subjects</a>
                                </td>
                                <td>
                                    <a class="nav-item nav-link" href="/schedules">Schedule</a>
                                </td>
                                <td>
                                    <a class="nav-item nav-link" href="/rooms">Rooms</a>
                                </td>
                                <td>
                                    <a class="nav-item nav-link" href="/buildings">Buildings</a>
                                </td>
                                <td>
                                    <a class="nav-item nav-link" href="/teachers">Teachers</a>
                                </td>
                                <td>
                                    <a class="nav-item nav-link" href="/lessons">Lessons</a>
                                </td>
                            </nav>
                        </div>
                    </div>
                </tr>
            </table>
        </div>
        <security:authorize access="hasRole('ROLE_ADMIN')">
            <div class="container justify-content-center" style="width: 80%">
                <div class="row justify-content-center">
                    <div class="text">
                        Admin's panel
                    </div>
                </div>
                <div class="row justify-content-center">
                    <nav class="nav nav-tabs">
                        <a class="nav-item nav-link" href="/messages">Messages</a>
                        <a class="nav-item nav-link" href="/admins">Create admin</a>
                        <a class="nav-item nav-link" href="/allProperties">Show properties of all modules</a>
                    </nav>
                </div>
            </div>
        </security:authorize>
    </body>

    </html>