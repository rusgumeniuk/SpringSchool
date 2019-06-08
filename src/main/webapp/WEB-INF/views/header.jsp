<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<table cellpadding="5" cellspacing="4" border="1" style="border-collapse:collapse">
    <tr>
        <tr>
            <td colspan="4" align="center">
                <a href="/">Ruslanius school</a>
            </td>
            <td>
                <security:authentication property="principal.username" />,
                <br> u r here,
                <br> welcome
            </td>
        </tr>
        <tr>
            <td><a href="/index">Main page</a></td>
            <td><a href="/students">Students</a></td>
            <td><a href="/groups">Groups</a></td>
            <td><a href="/subjects">Subjects</a></td>
            <td><a href="/rooms">Rooms</a></td>
            <td><a href="/buildings">Buildings</a></td>
            <td><a href="/teachers">Teachers</a></td>
            <td><a href="/logout">Log out</a></td>
        </tr>
    </tr>
</table>
<security:authorize access="hasRole('ROLE_ADMIN')">
    <table>
        <caption>Admin's panel</caption>
        <tr>
            <td><a href="/messages">Messages</a></td>
            <td><a href="/admins">Create admin</a> </td>
            <td><a href="/allProperties">Show properties of all modules</a></td>
        </tr>
    </table>

</security:authorize>