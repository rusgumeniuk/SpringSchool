<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<table cellpadding="5" cellspacing="4" border="1" style="border-collapse:collapse">
    <tr>
        <tr>
            <td colspan="4" align="center">
                Ruslanius school
            </td>
            <td>
                ${user.name},
                <br> u r here,
                <br> welcome
            </td>
        </tr>
        <tr>
            <td><a href="/index">Main page</a></td>
            <td><a href="/students">Students</a></td>
            <td><a href="/groups">Groups</a></td>
            <td><a href="/disciplines">Disciplines</a></td>
            <security:authorize access="hasRole('ROLE_ADMIN')">
                <td><a href="/messages">Messages</a></td>
                <td><a href="/admins">Create admin</a> </td>
            </security:authorize>
            <td><a href="/logout">Log out</a></td>
        </tr>
    </tr>
</table>