<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>Ocean View Resort - Login</title>
</head>
<body>
  <h2>Login</h2>

  <%
    String err = (String) request.getAttribute("error");
    if (err != null) {
  %>
      <p style="color:red;"><%= err %></p>
  <%
    }
  %>

  <form method="post" action="<%= request.getContextPath() %>/login">
    <label>Username:</label>
    <input type="text" name="username" required />
    <br/><br/>

    <label>Password:</label>
    <input type="password" name="password" required />
    <br/><br/>

    <button type="submit">Login</button>
  </form>
</body>
</html>
