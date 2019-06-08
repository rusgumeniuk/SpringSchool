<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<div>
    <h2>
        Designed by Ruslan Humeniuk
    </h2>
    <h3>Here u can leave a feedback:</h3>
    <ul>
        <li>t.me/mr.omman</li>
        <li>rus.gumeniuk@gmail.com</li>
    </ul>
</div>

<div>
    <div>
        <h1 align="right">
            Ruslanius school
        </h1>
        <h3>
            <security:authentication property="principal.username" />
        </h3>
        <h4>, u r here,</h4>
        <br>
        <h4>welcome</h4>
    </div>
    <br>
    <nav class="menu">
        <ul id="head">
            <li class="obj"><a href="/index">Main page</a></li>
            <li class="obj"><a href="/students">Students</a></li>
            <li class="obj"><a href="/groups">Groups</a></li>
            <li class="obj"><a href="/subjects">Subjects</a></li>
            <li class="obj"><a href="/rooms">Rooms</a></li>
            <li class="obj"><a href="/building">Buildings</a></li>
            <li class="obj"><a href="/teachers">Teachers</a></li>
            <li class="obj"><a href="/lessons">Lessons</a></li>
            <li class="obj"><a href="/logout">Log out</a> </li>
        </ul>
    </nav>
</div>