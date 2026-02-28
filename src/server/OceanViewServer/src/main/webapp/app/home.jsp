<%--
  Created by IntelliJ IDEA.
  User: gihan
  Date: 02/28/26
  Time: 15:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Home</title>
</head>
<body>
<h1>Login successful</h1>
<p>Welcome to Ocean View Admin Panel</p>

<p>
    <a href="${pageContext.request.contextPath}/api/health">Tomcat Server Status</a>
</p>

<p>
    <a href="${pageContext.request.contextPath}/logout">Logout</a>
</p>
</body>
</html>