<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>Ocean View Resort - Login</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/ui.css">
</head>
<body>
<div class="app-shell">
  <div class="container">

    <div class="topbar">
      <div class="brand">
        <div class="logo"></div>
        <div>
          <h1>Ocean View Resort</h1>
          <div class="sub">Staff Login</div>
        </div>
      </div>
      <div class="pill">🔒 Secure staff access</div>
    </div>

    <div class="card card-pad" style="max-width: 560px; margin: 0 auto;">
      <h2 class="page-title">Login</h2>
      <div class="page-subtitle">Enter your staff credentials to access the system.</div>

      <%
        String err = (String) request.getAttribute("error");
        if (err != null) {
      %>
      <div class="alert alert-err"><%= err %></div>
      <%
        }
      %>

      <form method="post" action="<%= request.getContextPath() %>/login">
        <div class="section" style="margin-top:14px;">
          <div style="display:grid; gap:12px;">
            <div>
              <label>Username</label>
              <input type="text" name="username" required />
            </div>

            <div>
              <label>Password</label>
              <input type="password" name="password" required />
            </div>

            <button class="btn btn-primary" type="submit">Login</button>
          </div>
        </div>
      </form>

    </div>

  </div>
</div>
</body>
</html>