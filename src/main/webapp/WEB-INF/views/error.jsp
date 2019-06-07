<%--
  Created by IntelliJ IDEA.
  User: Omman
  Date: 05.01.2019
  Time: 20:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Some error</title>
</head>
<body>
<h3>Oooops some error</h3>
<h1>${error}</h1>
<div class="goBack" >
    <button class="btn" onclick="location.href='../..'">Main page</button>
</div >
</body>
<jsp:include page="../views/footer.jsp" />
</html>
